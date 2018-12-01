//package business.jwtbenchmark
//
//import com.auth0.jwt.algorithms.Algorithm
//import kotlinx.coroutines.experimental.timeunit.TimeUnit
//import sun.misc.BASE64Decoder
//import sun.misc.BASE64Encoder
//import java.security.KeyFactory
//import java.security.KeyPairGenerator
//import java.security.PrivateKey
//import java.security.spec.PKCS8EncodedKeySpec
//import javax.crypto.Cipher
//import javax.crypto.Cipher.SECRET_KEY
//import javax.crypto.KeyGenerator
//import javax.crypto.SecretKeyFactory
//import javax.crypto.spec.PBEKeySpec
//
///**
// * 1.生成、管理jwt token的secret
// * 2.生成随机盐、获取密码摘要
// *
// * P.S:
// *    1.签发使用的secret两小时更新一次
// *    2.secret自身的时效为4小时，即token的时效也为4小时
// *
// * Created by Chendk on 2018/8/14
// */
//object SafetyUtils {
//
//    private const val SIGN_SECRET_INTERVAL_MILLS = 2 * 60 * 60 * 1000L
//    private const val SECRET_INTERVAL_MILLS = 4 * 60 * 60 * 1000L
//
//    private const val ALGORITHM_AES = "AES"
//
//    private const val ALGORITHM_RSA = "RSA"
//    private const val KEY_SIZE = 1024
//
//    private const val ALGORITHM_PBKDF2WithHmacSHA256 = "PBKDF2WithHmacSHA256"
//
//    private val PassRegex = Regex("^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()\\-_+=|{}'\":;,.\\[\\]<>/?]){6,20}$")
//
//    fun getJwtAlgorithm(): Algorithm {
//
//        return Algorithm.HMAC256(getSignSecret())
//    }
//
//    fun getJwtAlgorithm(secret: String): Algorithm {
//
//        return Algorithm.HMAC256(secret)
//    }
//
//    /**
//     * 获取当前系统签发token用secret
//     */
//    private fun getSignSecret(): String {
//        return generateSignSecret()
//    }
//
//    /**
//     * 获取系统所有未失效的secret
//     */
//    fun getAllSecret(): List<String> {
//        return redisTemplate.keys("$SECRET_KEY*").takeIf { !it.isNullOrEmpty() }?.let { keys ->
//            redisTemplate.opsForValue().let { valOps ->
//                keys.mapNotNull { valOps.get(it) }
//            }
//        } ?: emptyList()
//    }
//
//    fun generateSalt(pass: String): String {
//        return KeyGenerator.getInstance(ALGORITHM_AES).apply {
//            this.init(256, SecureRandom(pass.toByteArray()))
//        }.let {
//            BASE64Encoder().encodeBuffer(it.generateKey().encoded)
//        }.removeLineBreak()
//    }
//
//    /**
//     * 密码格式是否符合规范
//     */
//    fun isValidPass(pass: String): Boolean {
//        return PassRegex.matches(pass)
//    }
//
//    /**
//     * 获取加盐的密码摘要
//     */
//    fun getDigestedPass(pass: String, salt: String): String {
//        val spec = PBEKeySpec(pass.toCharArray(), salt.toByteArray(), 1000, 512)
//        return SecretKeyFactory.getInstance(ALGORITHM_PBKDF2WithHmacSHA256).let {
//            BASE64Encoder().encodeBuffer(it.generateSecret(spec).encoded)
//        }.removeLineBreak()
//    }
//
//    /**
//     * RSA 私钥解密
//     */
//    fun decryptPass(encryptedPass: String, privateKey: PrivateKey): String {
//        return Cipher.getInstance(ALGORITHM_RSA).apply {
//            this.init(Cipher.DECRYPT_MODE, privateKey)
//        }.let { cipher ->
//            BASE64Decoder().decodeBuffer(encryptedPass)
//                    .let {
//                        String(cipher.doFinal(it))
//                    }
//        }
//    }
//
//    fun buildPrivateKeyFromString(privateKeyStr: String?): PrivateKey? {
//        return privateKeyStr.runIfNotNullOrBlank {
//            KeyFactory.getInstance(ALGORITHM_RSA).generatePrivate(PKCS8EncodedKeySpec(BASE64Decoder().decodeBuffer(it)))
//        }
//    }
//
//    /**
//     * 获取BASE64编码过的RSA公私钥对
//     * first -> 公钥
//     * second -> 私钥
//     */
//    fun generateKeyPair(): Pair<String, String> {
//        //利用随机数据源初始化
//        return KeyPairGenerator.getInstance(ALGORITHM_RSA)
//                .apply {
//                    this.initialize(KEY_SIZE, SecureRandom())
//                }
//                .generateKeyPair()
//                .let {
//                    val publicKeyBytes = it.public.encoded
//                    val privateKeyBytes = it.private.encoded
//                    val encoder = BASE64Encoder()
//                    Pair(encoder.encodeBuffer(publicKeyBytes), encoder.encodeBuffer(privateKeyBytes).removeLineBreak())
//                }
//    }
//
//    private fun generateSignSecret(): String {
//        //1.构造密钥生成器，指定为AES算法,不区分大小写
//        val secret = KeyGenerator.getInstance(ALGORITHM_AES).apply {
//            this.init(256, SecureRandom())
//        }.let {
//            BASE64Encoder().encodeBuffer(it.generateKey().encoded).removeLineBreak()
//        }
//        redisTemplate.boundValueOps(secretKey)
//                .set(secret, SECRET_INTERVAL_MILLS, TimeUnit.MILLISECONDS)
//        return secret
//    }
//
//}