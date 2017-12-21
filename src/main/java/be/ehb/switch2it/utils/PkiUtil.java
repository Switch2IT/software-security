package be.ehb.switch2it.utils;

import be.ehb.switch2it.rest.exceptions.ExceptionFactory;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.bc.BcX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public final class PkiUtil {

    private static final Logger log = LoggerFactory.getLogger(PkiUtil.class);
    private static final String SECURE_RANDOM_ALGO = "SHA1PRNG";
    private static final String RSA = "RSA";
    private static final String SIGNATURE_ALGO = "SHA512withRSA";
    private static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----\n";
    private static final String END_CERT = "\n-----END CERTIFICATE-----\n";
    private static final String BEGIN_PRIV_KEY = "-----BEGIN RSA PRIVATE KEY-----\n";
    private static final String END_PRIV_KEY = "\n-----END RSA PRIVATE KEY-----";
    private static final Date NOT_BEFORE = new Date(
            System.currentTimeMillis() - 86400000L * 365);
    private static final Date NOT_AFTER = new Date(
            System.currentTimeMillis() + 86400000L * 365 * 100);

    private PkiUtil() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Creating a certificate");
        System.out.print("Enter the common name (CN): ");
        String commonName = scanner.nextLine();
        System.out.print("Enter the organization (O): ");
        String organization = scanner.nextLine();
        System.out.print("Enter the organizational unit (OU): ");
        String organizationUnit = scanner.nextLine();
        System.out.print("Enter keysize (2048 or 4096):");
        String keySizeString = scanner.nextLine();
        System.out.println("\n");

        KeySize keySize;
        switch (keySizeString) {
            case "2048":
                keySize = KeySize.k2048;
                break;
            case "4096":
                keySize = KeySize.k4096;
                break;
            default:
                throw ExceptionFactory.unsupportedKeySizeException(keySizeString);
        }

        try {
            createCert(commonName, organization, organizationUnit, keySize);
        } catch (Exception ex) {
            log.error("Something went wrong: ", ex);
        }
    }

    public static void createCert(String commonName, String organization, String organizationUnit, KeySize keysize) throws Exception {
        SecureRandom sr = SecureRandom.getInstance(SECURE_RANDOM_ALGO);

        KeyPair keyPair = generateKeyPair(keysize.getKeysize(), sr);


        X500NameBuilder nameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        nameBuilder.addRDN(BCStyle.CN, commonName);
        nameBuilder.addRDN(BCStyle.O, organization);
        nameBuilder.addRDN(BCStyle.OU, organizationUnit);

        X500Name issuer = nameBuilder.build();
        BigInteger serial = BigInteger.valueOf(initRandomSerial());
        PublicKey pubKey = keyPair.getPublic();

        X509v3CertificateBuilder generator = new JcaX509v3CertificateBuilder(
                issuer, serial, NOT_BEFORE, NOT_AFTER, issuer, pubKey);

        generator.addExtension(Extension.subjectKeyIdentifier, false,
                createSubjectKeyIdentifier(pubKey));
        generator.addExtension(Extension.basicConstraints, true,
                new BasicConstraints(true));

        KeyUsage usage = new KeyUsage(KeyUsage.keyCertSign
                | KeyUsage.digitalSignature | KeyUsage.keyEncipherment
                | KeyUsage.dataEncipherment | KeyUsage.cRLSign | KeyUsage.nonRepudiation | KeyUsage.digitalSignature);
        generator.addExtension(Extension.keyUsage, false, usage);

        ASN1EncodableVector purposes = new ASN1EncodableVector();
        purposes.add(KeyPurposeId.id_kp_serverAuth);
        purposes.add(KeyPurposeId.id_kp_clientAuth);
        purposes.add(KeyPurposeId.anyExtendedKeyUsage);
        generator.addExtension(Extension.extendedKeyUsage, false,
                new DERSequence(purposes));

        X509Certificate cert = signCertificate(generator, keyPair.getPrivate());

        System.out.println("Certificate: ");
        System.out.println(convertToPem(cert));

        System.out.println("Private key: ");
        System.out.println(convertToPem(keyPair.getPrivate()));
    }

    public static String convertToPem(X509Certificate cert) throws CertificateEncodingException {
        byte[] derCert = cert.getEncoded();
        String pemCertPre = Base64.toBase64String(derCert);
        return BEGIN_CERT + pemCertPre + END_CERT;
    }

    public static String convertToPem(PrivateKey priv) {
        byte[] derPriv = priv.getEncoded();
        String privPem = Base64.toBase64String(derPriv);
        return BEGIN_PRIV_KEY + privPem + END_PRIV_KEY;
    }

    public static KeyPair generateKeyPair(int keySize, SecureRandom secureRandom)
            throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator
                .getInstance(RSA);
        generator.initialize(keySize, secureRandom);
        return generator.generateKeyPair();
    }

    public static X509Certificate signCertificate(
            X509v3CertificateBuilder certificateBuilder,
            PrivateKey signedWithPrivateKey) throws OperatorCreationException, CertificateException {
        Security.addProvider(new BouncyCastleProvider());
        ContentSigner signer = new JcaContentSignerBuilder(SIGNATURE_ALGO)
                .setProvider(BouncyCastleProvider.PROVIDER_NAME).build(signedWithPrivateKey);
        X509Certificate cert = new JcaX509CertificateConverter().setProvider(
                BouncyCastleProvider.PROVIDER_NAME).getCertificate(certificateBuilder.build(signer));
        return cert;
    }

    public static SubjectKeyIdentifier createSubjectKeyIdentifier(Key key)
            throws IOException {
        ByteArrayInputStream bIn = new ByteArrayInputStream(key.getEncoded());

        try (ASN1InputStream is = new ASN1InputStream(bIn)) {
            ASN1Sequence seq = (ASN1Sequence) is.readObject();
            SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(seq);
            return new BcX509ExtensionUtils().createSubjectKeyIdentifier(info);
        }
    }

    public static long initRandomSerial() {
        final Random rnd = new Random();
        rnd.setSeed(System.currentTimeMillis());
        // prevent browser certificate caches, cause of doubled serial numbers
        // using 48bit random number
        long sl = ((long) rnd.nextInt()) << 32 | (rnd.nextInt() & 0xFFFFFFFFL);
        // let reserve of 16 bit for increasing, serials have to be positive
        sl = sl & 0x0000FFFFFFFFFFFFL;
        return sl;
    }

    public enum KeySize {
        k2048(2048), k4096(4096);

        private int keysize;

        KeySize(int keysize) {
            this.keysize = keysize;
        }

        public int getKeysize() {
            return keysize;
        }
    }
}