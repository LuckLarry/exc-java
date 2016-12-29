package com.gnete.merchant.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @author Administrator
 * 
 */
public class Crypt
{

	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
	private Logger log = Logger.getLogger(Crypt.class);

	/**
	 * 构造函数
	 */
	public Crypt()
	{
		Security.addProvider(new BouncyCastleProvider());
	}

	private boolean isConvertEncode = true;
	/**
	 * 取出上次调用加密、解密、签名函数成功后的输出结果
	 */
	protected String lastResult;

	/**
	 * 取出上次调用任何函数失败后的失败原因
	 */
	protected String lastErrMsg;

	/**
	 * 返回上一次签名结果
	 */
	protected String lastSignMsg;

	private boolean isTestServer = true;

	/**
	 * 对字符串进行加密
	 * 
	 * @param TobeEncrypted
	 *            待加密的字符串
	 * @param CertFile
	 *            解密者公钥证书路径
	 * @return 加密成功返回true(从LastResult属性获取结果)，失败返回false(从LastErrMsg属性获取失败原因)
	 */
	// 加密，默认UTF-8编码
	// 商户提交上来的交易是默认用UTF-8编码
	public boolean EncryptMsg(final String TobeEncrypted, final String CertFile)
	{
		return EncryptMsgByCertFilePath(TobeEncrypted, CertFile, "UTF-8");
	}

	public boolean EncryptMsg(final String TobeEncrypted, final String CertFile, final String Charset)
	{
		return EncryptMsgByCertFilePath(TobeEncrypted, CertFile, Charset);
	}

	private boolean EncryptMsgByCertFilePath(final String TobeEncrypted, final String CertFile, final String Charset)
	{
		boolean result = false;
		FileInputStream certfile = null;
		try
		{
			certfile = new FileInputStream(CertFile);

			CertificateFactory cf = CertificateFactory.getInstance("X.509");

			X509Certificate x509cert = (X509Certificate) cf.generateCertificate(certfile);
			RSAPublicKey pubkey = (RSAPublicKey) x509cert.getPublicKey();
			BigInteger mod = pubkey.getModulus();

			int keylen = mod.bitLength() / 8;
			if (TobeEncrypted.length() > keylen - 11)
			{
				// 创建Cipher 对象（确定算法[RSA]、方式[NONE]和填充[PKCS1Padding]）
				Cipher pub = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
				pub.init(Cipher.WRAP_MODE, pubkey);

				// get a DES private key
				KeyGenerator kp = KeyGenerator.getInstance("DESEDE");
				kp.init(new SecureRandom());// SecureRandom提供加密的强随机数生成器 (RNG)
				SecretKey sk = kp.generateKey();
				// 将密钥包装
				byte wrappedKey[] = pub.wrap(sk);

				// 创建Cipher 对象（确定算法[DESEDE]、方式[OFB:输出反馈方式(Output Feedback
				// Mode)]和填充[NoPadding]）
				pub = Cipher.getInstance("DESEDE/OFB/NoPadding");
				pub.init(Cipher.ENCRYPT_MODE, sk);

				// int input = TobeEncrypted.length();
				// 按单部分操作加密或解密数据，或者结束一个多部分操作。数据被加密还是解密取决于此 cipher 的初始化方式
				byte encrypted[] = pub.doFinal(TobeEncrypted.getBytes(Charset));
				byte iv[] = pub.getIV();// 返回新缓冲区中的初始化向量 (IV)
				byte enc_ascii[] = new byte[encrypted.length * 2];
				byte iv_asc[] = new byte[iv.length * 2];
				byte prikey_asc[] = new byte[wrappedKey.length * 2];
				Hex2Ascii(encrypted.length, encrypted, enc_ascii);
				Hex2Ascii(iv.length, iv, iv_asc);
				Hex2Ascii(wrappedKey.length, wrappedKey, prikey_asc);
				this.lastResult = new String(iv_asc) + new String(prikey_asc) + new String(enc_ascii);
			}
			else
			{
				Cipher pub = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
				pub.init(Cipher.ENCRYPT_MODE, pubkey);
				byte encrypted[] = pub.doFinal(TobeEncrypted.getBytes());
				byte enc_ascii[] = new byte[encrypted.length * 2];
				Hex2Ascii(encrypted.length, encrypted, enc_ascii);
				this.lastResult = new String(enc_ascii);

			}
			result = true;
		}
		catch (CertificateException e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10016, Error Description: ER_CERT_PARSE_ERROR（证书解析错误）";
			result = false;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10005, Error Description: ER_FIND_CERT_FAILED（找不到证书）";
			result = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10022, Error Description: ER_ENCRYPT_ERROR（加密失败）" + e.toString();
			result = false;
		}
		finally
		{
			try
			{
				if (!certfile.equals(null))
				{
					certfile.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				this.lastErrMsg = "Error Number:-10030, Error Description: ER_CLOSEFILE_ERROR（证书文件关闭失败）";
				result = false;
			}

		}
		return result;
	}


	/**
	 * 对加密后的密文进行解密
	 * 
	 * @param TobeDecrypted
	 *            需要解密的密文
	 * @param KeyFile
	 *            PFX证书文件路径
	 * @param PassWord
	 *            私钥保护密码
	 * @return 解密成功返回true(从LastResult属性获取结果)，失败返回false(从LastErrMsg属性获取失败原因)
	 */
	// GBK编码
	// 默认情况下，使用GBK编码
	public boolean DecryptMsg(final String TobeDecrypted, final String KeyFile, final String PassWord)
	{
		return DecryptMsgByFilePath(TobeDecrypted, KeyFile, PassWord, "GBK");
	}

	public boolean DecryptMsg(final String TobeDecrypted, final String KeyFile, final String PassWord, final String Charset)
	{
		return DecryptMsgByFilePath(TobeDecrypted, KeyFile, PassWord, Charset);
	}

	public boolean DecryptMsgByFilePath(String TobeDecrypted, final String KeyFile, final String PassWord, final String Charset)
	{
		boolean result = false;
		FileInputStream fiKeyFile = null;
		try
		{
			KeyStore ks = KeyStore.getInstance("PKCS12");
			fiKeyFile = new FileInputStream(KeyFile);
			ks.load(fiKeyFile, PassWord.toCharArray());
			Enumeration<String> myEnum = ks.aliases();
			String keyAlias = null;
			RSAPrivateCrtKey prikey = null;
			/* IBM JDK必须使用While循环取最后一个别名，才能得到个人私钥别名 */
			while (myEnum.hasMoreElements())
			{
				keyAlias = (String) myEnum.nextElement();
				if (ks.isKeyEntry(keyAlias))
				{
					prikey = (RSAPrivateCrtKey) ks.getKey(keyAlias, PassWord.toCharArray());
					break;
				}
			}
			if (prikey == null)
			{
				this.lastErrMsg = "Error Number:-10015, Error Description: ER_PRIKEY_CANNOT_FOUND（没有找到匹配私钥）";
				result = false;
			}
			else
			{
				BigInteger mod = prikey.getModulus();
				int keylen = mod.bitLength() / 8;
				if (TobeDecrypted.length() > keylen * 2)
				{
					byte iv_asc[] = TobeDecrypted.substring(0, 16).getBytes();
					byte prikey_asc[] = TobeDecrypted.substring(iv_asc.length, iv_asc.length + keylen * 2).getBytes();
					byte enc_ascii[] = TobeDecrypted.substring(iv_asc.length + keylen * 2).getBytes();

					byte iv[] = new byte[8];
					byte unwrappedkey[] = new byte[prikey_asc.length / 2];
					byte decrypted[] = new byte[enc_ascii.length / 2];

					 Ascii2Hex(iv_asc.length, iv_asc, iv);
					 Ascii2Hex(prikey_asc.length, prikey_asc, unwrappedkey);
					 Ascii2Hex(enc_ascii.length, enc_ascii, decrypted);

					Cipher pri = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
					pri.init(Cipher.UNWRAP_MODE, prikey);
					Key unwrappedKey = pri.unwrap(unwrappedkey, "DESEDE", Cipher.SECRET_KEY);
					// 此类指定一个初始化向量 (IV)。使用 IV 的例子是反馈模式中的密码，如，CBC 模式中的 DES 和使用OAEP
					// 编码操作的 RSA 密码
					IvParameterSpec ivspec = new IvParameterSpec(iv);
					pri = Cipher.getInstance("DESEDE/OFB/NoPadding");
					pri.init(Cipher.DECRYPT_MODE, unwrappedKey, ivspec);
					this.lastResult = new String(pri.doFinal(decrypted), Charset);
				}
				else
				{
					Cipher pri = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
					pri.init(Cipher.DECRYPT_MODE, prikey);
					byte enc_ascii[] = TobeDecrypted.getBytes();
					byte decrypted[] = new byte[enc_ascii.length / 2];
					Ascii2Hex(enc_ascii.length, enc_ascii, decrypted);
					byte decryptout[] = pri.doFinal(decrypted);
					this.lastResult = new String(decryptout, Charset);
				}
				result = true;
			}

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10005, Error Description: ER_FIND_CERT_FAILED（找不到证书）";
			result = false;
		}
		catch (UnrecoverableKeyException e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10015, Error Description: ER_PRIKEY_CANNOT_FOUND（没有找到匹配私钥）";
			result = false;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10015, Error Description: ER_PRIKEY_CANNOT_FOUND（没有找到匹配私钥）";
			result = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10023, Error Description: ER_DECRYPT_ERROR（解密失败）";
			result = false;
		}
		finally
		{
			try
			{
				if (!fiKeyFile.equals(null))
				{
					fiKeyFile.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				this.lastErrMsg = "Error Number:-10030, Error Description: ER_CLOSEFILE_ERROR（证书文件关闭失败）";
				result = false;
			}
		}
		return result;
	}

	/**
	 * 对字符串进行签名
	 * 
	 * @param TobeSigned
	 *            需要进行签名的字符串
	 * @param KeyFile
	 *            PFX证书文件路径
	 * @param PassWord
	 *            私钥保护密码
	 * @return 签名成功返回true(从LastResult属性获取结果)，失败返回false(从LastErrMsg属性获取失败原因)
	 */
	public boolean SignMsg(final String TobeSigned, final String KeyFile, final String PassWord)
	{
		return SignMsgByFilePath(TobeSigned, KeyFile, PassWord, "GBK");
	}
	public boolean SignMsg(final String TobeSigned, final String KeyFile, final String PassWord, String Charset)
	{
		return SignMsgByFilePath(TobeSigned, KeyFile, PassWord, Charset);
	}
	private boolean SignMsgByFilePath(final String TobeSigned, final String KeyFile, final String PassWord, String Charset)
	{
		boolean result = false;
		FileInputStream fiKeyFile = null;
		try
		{
			this.lastSignMsg = "";
			KeyStore ks = KeyStore.getInstance("PKCS12");
			// ks.load(new FileInputStream(KeyFile), PassWord.toCharArray());
			fiKeyFile = new FileInputStream(KeyFile);
			ks.load(fiKeyFile, PassWord.toCharArray());
			Enumeration<String> myEnum = ks.aliases();
			String keyAlias = null;
			RSAPrivateCrtKey prikey = null;
			// keyAlias = (String) myEnum.nextElement();
			/* IBM JDK必须使用While循环取最后一个别名，才能得到个人私钥别名 */
			while (myEnum.hasMoreElements())
			{
				keyAlias = (String) myEnum.nextElement();
				// System.out.println("keyAlias==" + keyAlias);
				if (ks.isKeyEntry(keyAlias))
				{
					prikey = (RSAPrivateCrtKey) ks.getKey(keyAlias, PassWord.toCharArray());
					break;
				}
			}
			if (prikey == null)
			{
				this.lastErrMsg = "Error Number:-10015, Error Description: ER_PRIKEY_CANNOT_FOUND（没有找到匹配私钥）";
				result = false;
			}
			else
			{
				Signature sign = Signature.getInstance("SHA1withRSA");
				sign.initSign(prikey);
				sign.update(TobeSigned.getBytes(Charset));
				byte signed[] = sign.sign();
				byte sign_asc[] = new byte[signed.length * 2];
				Hex2Ascii(signed.length, signed, sign_asc);
				this.lastResult = new String(sign_asc);
				this.lastSignMsg = this.lastResult;
				result = true;
			}

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10005, Error Description: ER_FIND_CERT_FAILED（找不到证书）";
			result = false;
		}
		catch (UnrecoverableKeyException e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10015, Error Description: ER_PRIKEY_CANNOT_FOUND（没有找到匹配私钥） | Exception:" + e.getMessage();
			result = false;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10015, Error Description: ER_PRIKEY_CANNOT_FOUND（没有找到匹配私钥） | Exception:" + e.getMessage();
			result = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10020, Error Description: ER_SIGN_ERROR（签名失败）" + e.toString() + "| Exception:" + e.getMessage();
			result = false;
		}
		finally
		{
			try
			{
				if (fiKeyFile != null && !fiKeyFile.equals(null))
				{
					fiKeyFile.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				this.lastErrMsg = "Error Number:-10030, Error Description: ER_CLOSEFILE_ERROR（证书文件关闭失败）";
				result = false;
			}
		}
		return result;
	}

	/**
	 * 验证签名
	 * 
	 * @param TobeVerified
	 *            待验证签名的密文
	 * @param PlainText
	 *            待验证签名的明文
	 * @param CertFile
	 *            签名者公钥证书 (证书路径)
	 * @return 验证成功返回true，失败返回false(从LastErrMsg属性获取失败原因)
	 */
	public boolean VerifyMsg(final String TobeVerified, final String PlainText, final String CertFile)
	{
		boolean result = false;
		FileInputStream certfile = null;
		try
		{
			certfile = new FileInputStream(CertFile);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate x509cert = (X509Certificate) cf.generateCertificate(certfile);
			RSAPublicKey pubkey = (RSAPublicKey) x509cert.getPublicKey();

			Signature verify = Signature.getInstance("SHA1withRSA");
			verify.initVerify(pubkey);
			byte signeddata[] = new byte[TobeVerified.length() / 2];
			Ascii2Hex(TobeVerified.length(), TobeVerified.getBytes(), signeddata);
			verify.update(PlainText.getBytes());
			if (verify.verify(signeddata))
			{
				result = true;
			}
			else
			{
				this.lastErrMsg = "Error Number:-10021, Error Description:ER_VERIFY_ERROR（验签失败）";
				result = false;
			}

		}
		catch (CertificateException e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10016, Error Description: ER_CERT_PARSE_ERROR（证书解析错误）";
			result = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10021, Error Description: ER_VERIFY_ERROR（验签失败）";
			result = false;
		}
		finally
		{
			try
			{
				if (!certfile.equals(null))
				{
					certfile.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				this.lastErrMsg = "Error Number:-10021, Error Description: ER_VERIFY_ERROR（验签失败）";
				result = false;
			}

		}
		return result;
	}
	

	public boolean VerifyMsg(final String TobeVerified, final String PlainText, final String CertFile, String Charset)
	{
		boolean result = false;
		FileInputStream certfile = null;
		try
		{
			certfile = new FileInputStream(CertFile);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate x509cert = (X509Certificate) cf.generateCertificate(certfile);
			RSAPublicKey pubkey = (RSAPublicKey) x509cert.getPublicKey();

			Signature verify = Signature.getInstance("SHA1withRSA");
			verify.initVerify(pubkey);
			byte signeddata[] = new byte[TobeVerified.length() / 2];
			Ascii2Hex(TobeVerified.length(), TobeVerified.getBytes(), signeddata);
			verify.update(PlainText.getBytes(Charset));
			if (verify.verify(signeddata))
			{
				result = true;
			}
			else
			{
				this.lastErrMsg = "Error Number:-10021, Error Description:ER_VERIFY_ERROR（验签失败）";
				result = false;
			}

		}
		catch (CertificateException e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10016, Error Description: ER_CERT_PARSE_ERROR（证书解析错误）";
			result = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10021, Error Description: ER_VERIFY_ERROR（验签失败）";
			result = false;
		}
		finally
		{
			try
			{
				if (!certfile.equals(null))
				{
					certfile.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				this.lastErrMsg = "Error Number:-10021, Error Description: ER_VERIFY_ERROR（验签失败）";
				result = false;
			}

		}
		return result;
	}

	/**
	 * 通过公钥串进行验签（ASN.1采用Tag,Lenth,Value,编码方式）
	 * 
	 * 
	 * 30:81:89:02:81:81:00:e7:79:a8:09:b0:94:9a:1f:09:f9:8b:e3:
	 * 4e:5d:be:8b:c1:63:b5: a0:39:23:d5:4c:8d:64:38:a3:2b:d8:d6:06:
	 * 6e:2f:a4:46:6c:4c:68:24:76:53:1d:55:d8: d4:
	 * 8e:e6:59:26:08:70:3a:9e:83:94:0c:3d:37:7d:dc:91:e8:78:7c:ef:68:3f:4a:2a:57
	 * : f8:78:
	 * 7e:ec:9f:6f:3a:47:66:4d:82:3c:28:cd:bb:5d:3f:48:28:41:a3:59:72:a2:9b:25:
	 * d:97:11:ab:16:3c:43:96:85:b8:ec:f9:4c:cd:81:c5:1f:6d:23:cd:cd:71:58:09:71
	 * :04: 4c:3a:0c:47:7d:02:03:01:00:01
	 * 
	 * ASN.1采用Tag,Lenth,Value,编码方式，在此将整个编为一个sequence，可以理解为结构体
	 * 以30作为开始标志，第二位81代表后面有1字节代表长度，即89代表长度（若为82则代表后面有两字节代表长度，依次类推）
	 * ，转化成十进制为137，正好与后面的字节数吻合，从第四位02开始便是此sequence的内涵，相当于结构体的元素，
	 * 一般来说sequence往往需要嵌套，相当于结构体嵌结构体，但对公钥的sequence来说，此处仅有一层。
	 * 第四位02代表一下的内容为bit流，同样紧随其后的81代表有一字节代表长度，
	 * 第六位的81代表长度为129，即从00开始直到最后一行7d此为129字节，去掉前面的00，余下128位便是rsa公钥的N值，
	 * 最后5个字节同样是bit流，以02开始，03表示长度为3，最后的01 00 01 便是rsa公钥的E值。
	 * 
	 * 0x89=137 0x81=129 5*26+10=140 20+26*4=20+104+5=129
	 * 
	 * @param TobeVerified
	 * @param PlainText
	 * @param strPubKey
	 * @return
	 */
	public boolean VerifyMsgPubKey(final String TobeVerified, final String PlainText, final String strPubKey)
	{
		return VerifyMsgPubKeyText(TobeVerified, PlainText, strPubKey, "GBK");
	}
	public boolean VerifyMsgPubKey(final String TobeVerified, final String PlainText, final String strPubKey, String Charset)
	{
		return VerifyMsgPubKeyText(TobeVerified, PlainText, strPubKey, Charset);
	}
	private boolean VerifyMsgPubKeyText(final String TobeVerified, final String PlainText, final String strPubKey, String Charset)
	{
		boolean result = false;
		try
		{
			if (strPubKey == null || StringUtils.isEmpty(strPubKey))
			{
				this.lastErrMsg = "Error Number:-10001, Error Description: ER_PUBKEY_ERROR（公钥串格式错误）";
				return false;
			}
			String Tag = strPubKey.substring(0, 2); // 30作为开始标志
			if (!"30".equals(Tag))
			{
				this.lastErrMsg = "Error Number:-10002, Error Description: ER_PUBKEY_ERROR（公钥串格式错误）";
				return false;
			}

			int LenBit = Integer.parseInt(strPubKey.substring(2, 4)) - 80; // 多少位表示“公钥体”的长度
			int BodyStart = 2 + 2 + LenBit * 2; // "公钥体"第几位的开始位置
			String Body = strPubKey.substring(BodyStart, strPubKey.length()); // "公钥体"

			log.info("【PUBLIC-KEY】" + Body);
			int BodyLen = Integer.parseInt(Body.substring(2, 2 + ((Integer.parseInt(Body.substring(2, 4)) - 80) * 2)), 16);
			int ValidStart = 2 + 2 + ((Integer.parseInt(Body.substring(2, 4)) - 80) * 2);
			String Modulus = Body.substring(ValidStart, ValidStart + (BodyLen * 2));
			log.info("【公钥 - Modulus】" + Modulus);

			String PublicExponentBody = Body.substring(ValidStart + (BodyLen * 2), Body.length());
			int PublicExponentLen = Integer.parseInt(PublicExponentBody.substring(2, 4), 16);
			String PublicExponent = PublicExponentBody.substring(4, 4 + (PublicExponentLen * 2));
			log.info("【公钥 - PublicExponent】" + PublicExponent);

			KeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(Modulus, 16), new BigInteger(PublicExponent, 16));
			KeyFactory factory = KeyFactory.getInstance("RSA");
			RSAPublicKey pubkey = (RSAPublicKey) factory.generatePublic(publicKeySpec);

			log.info("【公钥RSAPublicKey】" + pubkey);

			Signature verify = Signature.getInstance("SHA1withRSA");
			verify.initVerify(pubkey);
			byte signeddata[] = new byte[TobeVerified.length() / 2];
			Ascii2Hex(TobeVerified.length(), TobeVerified.getBytes(), signeddata);
			verify.update(PlainText.getBytes(Charset));
			if (verify.verify(signeddata))
			{
				result = true;
			}
			else
			{
				this.lastErrMsg = "Error Number:-10021, Error Description:ER_VERIFY_ERROR（验签失败）";
				result = false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.lastErrMsg = "Error Number:-10021, Error Description: ER_VERIFY_ERROR（验签失败）";
			result = false;
		}
		return result;
	}

	/**
	 * 返回上次调用加密、解密、签名函数成功后的输出结果
	 * 
	 * @return 返回上次调用加密、解密、签名函数成功后的输出结果
	 */
	public String getLastResult()
	{
		return this.lastResult;
	}

	/**
	 * 返回上次调用任何函数失败后的失败原因
	 * 
	 * @return 返回上次调用任何函数失败后的失败原因
	 */
	public String getLastErrMsg()
	{
		return this.lastErrMsg;
	}

	/**
	 * 返回上一次签名结果
	 * 
	 * @return 签名结果
	 */
	public String getLastSignMsg()
	{
		return this.lastSignMsg;
	}

	/**
	 * 将十六进制数据转换成ASCII字符串
	 * 
	 * @param len
	 *            十六进制数据长度
	 * @param data_in
	 *            待转换的十六进制数据
	 * @param data_out
	 *            已转换的ASCII字符串
	 */
	private static void Hex2Ascii(int len, byte data_in[], byte data_out[])
	{
		byte temp1[] = new byte[1];
		byte temp2[] = new byte[1];
		for (int i = 0, j = 0; i < len; i++)
		{
			temp1[0] = data_in[i];
			temp1[0] = (byte) (temp1[0] >>> 4);
			temp1[0] = (byte) (temp1[0] & 0x0f);
			temp2[0] = data_in[i];
			temp2[0] = (byte) (temp2[0] & 0x0f);
			if (temp1[0] >= 0x00 && temp1[0] <= 0x09)
			{
				(data_out[j]) = (byte) (temp1[0] + '0');
			}
			else if (temp1[0] >= 0x0a && temp1[0] <= 0x0f)
			{
				(data_out[j]) = (byte) (temp1[0] + 0x57);
			}

			if (temp2[0] >= 0x00 && temp2[0] <= 0x09)
			{
				(data_out[j + 1]) = (byte) (temp2[0] + '0');
			}
			else if (temp2[0] >= 0x0a && temp2[0] <= 0x0f)
			{
				(data_out[j + 1]) = (byte) (temp2[0] + 0x57);
			}
			j += 2;
		}
	}

	/**
	 * 将ASCII字符串转换成十六进制数据
	 * 
	 * @param len
	 *            ASCII字符串长度
	 * @param data_in
	 *            待转换的ASCII字符串
	 * @param data_out
	 *            已转换的十六进制数据
	 */
	public static void Ascii2Hex(int len, byte data_in[], byte data_out[])
	{
		byte temp1[] = new byte[1];
		byte temp2[] = new byte[1];
		for (int i = 0, j = 0; i < len; j++)
		{
			temp1[0] = data_in[i];
			temp2[0] = data_in[i + 1];
			if (temp1[0] >= '0' && temp1[0] <= '9')
			{
				temp1[0] -= '0';
				temp1[0] = (byte) (temp1[0] << 4);

				temp1[0] = (byte) (temp1[0] & 0xf0);

			}
			else if (temp1[0] >= 'a' && temp1[0] <= 'f')
			{
				temp1[0] -= 0x57;
				temp1[0] = (byte) (temp1[0] << 4);
				temp1[0] = (byte) (temp1[0] & 0xf0);
			}

			if (temp2[0] >= '0' && temp2[0] <= '9')
			{
				temp2[0] -= '0';

				temp2[0] = (byte) (temp2[0] & 0x0f);

			}
			else if (temp2[0] >= 'a' && temp2[0] <= 'f')
			{
				temp2[0] -= 0x57;

				temp2[0] = (byte) (temp2[0] & 0x0f);
			}
			data_out[j] = (byte) (temp1[0] | temp2[0]);

			i += 2;
		}

	}

	protected String replaceAll(String strURL, String strAugs)
	{

		// JDK1.3中String类没有replaceAll的方法
		/** ********************************************************** */
		int start = 0;
		int end = 0;
		String temp = new String();
		while (start < strURL.length())
		{
			end = strURL.indexOf(" ", start);
			if (end != -1)
			{
				temp = temp.concat(strURL.substring(start, end).concat("%20"));
				if ((start = end + 1) >= strURL.length())
				{
					strURL = temp;
					break;
				}

			}
			else if (end == -1)
			{
				if (start == 0)
					break;
				if (start < strURL.length())
				{
					temp = temp.concat(strURL.substring(start, strURL.length()));
					strURL = temp;
					break;
				}
			}

		}

		temp = "";
		start = end = 0;

		while (start < strAugs.length())
		{
			end = strAugs.indexOf(" ", start);
			if (end != -1)
			{
				temp = temp.concat(strAugs.substring(start, end).concat("%20"));
				if ((start = end + 1) >= strAugs.length())
				{
					strAugs = temp;
					break;
				}

			}
			else if (end == -1)
			{
				if (start == 0)
					break;
				if (start < strAugs.length())
				{
					temp = temp.concat(strAugs.substring(start, strAugs.length()));
					strAugs = temp;
					break;
				}
			}

		}

		/** **************************************************************** */
		return strAugs;
	}

	/**
	 * 读取配置文件信息
	 * 
	 * @param key
	 *            需要获取的参数名称
	 * @param propFile
	 *            参数文件
	 * @return 返回参数相应的值
	 */
	protected String getProperties(String key, String propFile)
	{
		ResourceBundle rb = null;
		if (this.isTestServer == true)
		{
			rb = ResourceBundle.getBundle(propFile + "Test");
		}
		else
		{
			rb = ResourceBundle.getBundle(propFile);
		}
		return rb.getString(key);
	}

	/**
	 * 检查当前使用的配置文件是否是测试配置文件
	 * 
	 * @return true：使用的是测试配置文件；false：使用的是生产配置文件
	 */
	public boolean isTestServer()
	{
		return isTestServer;
	}

	/**
	 * 设置读取的配置文件（true：测试配置文件；false：生产配置文件），默认为true
	 * 
	 * @param isTestServer
	 */
	public void setTestServer(boolean isTestServer)
	{
		this.isTestServer = isTestServer;
	}

	/**
	 * 检查是否将服务器返回结果转换为UTF8编码
	 * 
	 * @return true：转换；false：不转换
	 */
	public boolean isConvertEncode()
	{
		return isConvertEncode;
	}

	/**
	 * 设置服务器返回结果是否转换为UTF8编码（true：转换为UTF8；false：不转换），默认为true
	 * 
	 * @param isConvertEncode
	 */
	public void setConvertEncode(boolean isConvertEncode)
	{
		this.isConvertEncode = isConvertEncode;
	}
	
	
	public String GetCerText( final String CertFile )
	{
		FileInputStream certfile = null;
		try
		{
			certfile = new FileInputStream(CertFile);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate x509cert = (X509Certificate) cf.generateCertificate(certfile);
			RSAPublicKey pubkey = (RSAPublicKey) x509cert.getPublicKey();
			String Text = new String(Hex.encodeHex (pubkey.getEncoded()));
			Text = Text.replace("30819f300d06092a864886f70d010101050003818d00", "");
			return Text.toUpperCase();
		}
		catch (CertificateException e)
		{
			e.printStackTrace();
			log.error(e.toString());
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error(e.toString());
			return null;
		}
		finally
		{
			try
			{
				if (!certfile.equals(null))
				{
					certfile.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				log.error(e.toString());
				return null;
			}
		}
	}
	
	public String[] getPublicKeyModulusExponent(String strPubKey)
	{
		int LenBit = Integer.parseInt(strPubKey.substring(2, 4)) - 80; // 多少位表示“公钥体”的长度
		int BodyStart = 2 + 2 + LenBit * 2; // "公钥体"第几位的开始位置
		String Body = strPubKey.substring(BodyStart, strPubKey.length()); // "公钥体"

		System.out.println("【PUBLIC-KEY】" + Body);
		int BodyLen = Integer.parseInt(Body.substring(2, 2 + ((Integer.parseInt(Body.substring(2, 4)) - 80) * 2)), 16);
		int ValidStart = 2 + 2 + ((Integer.parseInt(Body.substring(2, 4)) - 80) * 2);
		String Modulus = Body.substring(ValidStart, ValidStart + (BodyLen * 2));
		System.out.println("【公钥 - Modulus】" + Modulus);

		String PublicExponentBody = Body.substring(ValidStart + (BodyLen * 2), Body.length());
		int PublicExponentLen = Integer.parseInt(PublicExponentBody.substring(2, 4), 16);
		String PublicExponent = PublicExponentBody.substring(4, 4 + (PublicExponentLen * 2));
		System.out.println("【公钥 - PublicExponent】" + PublicExponent);
		
		return new String[]{Modulus, PublicExponent};
	}
	
	public RSAPrivateCrtKey getPrivateKey(String PrivateKey, String Password)
	{
		FileInputStream fiKeyFile = null;
		RSAPrivateCrtKey prikey = null;
		try
		{
			KeyStore ks = KeyStore.getInstance("PKCS12");
			fiKeyFile = new FileInputStream(PrivateKey);
			ks.load(fiKeyFile, Password.toCharArray());
			Enumeration<String> myEnum = ks.aliases();
			String keyAlias = null;
			while (myEnum.hasMoreElements())
			{
				keyAlias = (String) myEnum.nextElement();
				if (ks.isKeyEntry(keyAlias))
				{
					prikey = (RSAPrivateCrtKey) ks.getKey(keyAlias, Password.toCharArray());
					break;
				}
			}
		}catch(Exception e)
		{
			return null;
		}
		return prikey;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		String strPubKey = "30818902818100c8b527bdd7e75fd48a93996c70fca68ab59337e73f5892db2efb39b1c1f7d9991a9af65956fb9cfeb9e685706ec20ee3d576cd4029d8fdc329cf4e7731b59d845b27231c8dbb8a62dcb90d3489ed0e12fe245bfe31dbd612a6b2c302137fc82cb38b817e1ba75adec1087b095bb547557f517529a820783846ea3bc387ed310b0203010001".toUpperCase();
		String Tag = strPubKey.substring(0, 2); // 30作为开始标志

		int LenBit = Integer.parseInt(strPubKey.substring(2, 4)) - 80; // 多少位表示“公钥体”的长度
		int BodyStart = 2 + 2 + LenBit * 2; // "公钥体"第几位的开始位置
		String Body = strPubKey.substring(BodyStart, strPubKey.length()); // "公钥体"

		System.out.println("【PUBLIC-KEY】" + Body);
		int BodyLen = Integer.parseInt(Body.substring(2, 2 + ((Integer.parseInt(Body.substring(2, 4)) - 80) * 2)), 16);
		int ValidStart = 2 + 2 + ((Integer.parseInt(Body.substring(2, 4)) - 80) * 2);
		String Modulus = Body.substring(ValidStart, ValidStart + (BodyLen * 2));
		System.out.println("【公钥 - Modulus】" + Modulus);

		String PublicExponentBody = Body.substring(ValidStart + (BodyLen * 2), Body.length());
		int PublicExponentLen = Integer.parseInt(PublicExponentBody.substring(2, 4), 16);
		String PublicExponent = PublicExponentBody.substring(4, 4 + (PublicExponentLen * 2));
		System.out.println("【公钥 - PublicExponent】" + PublicExponent);

		KeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(Modulus, 16), new BigInteger(PublicExponent, 16));
		KeyFactory factory = KeyFactory.getInstance("RSA");
		RSAPublicKey pubkey = (RSAPublicKey) factory.generatePublic(publicKeySpec);

		System.out.println("【公钥RSAPublicKey】" + pubkey);
	} 

}
