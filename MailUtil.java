package ma.dataprotect.sensipro.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import ma.dataprotect.sensipro.model.EmailToResend;
import ma.dataprotect.sensipro.model.Organism;
import ma.dataprotect.sensipro.model.TypeCompte;
import ma.dataprotect.sensipro.rapport.objects.ParameterVO;
import ma.dataprotect.sensipro.services.EmailToResendService;
import ma.dataprotect.sensipro.services.ParameterService;

@Component
public class MailUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);

	private ParameterVO parameterVO;
	private static int envois = 0;

	@Autowired
	private EmailToResendService emailToResendService;

	@Autowired
	private ParameterService parameterService;

	public void initParam() throws Exception {
		parameterVO = parameterService.getParameterVO();
	}

	public ParameterVO getParameterVO() {
		return parameterVO;
	}
	
	public void send(final String to, final List<String> tocc, final String object, final String text,
			final Organism org, final Boolean fSendMailToResend) {
		send(this.parameterVO, to, tocc, object, text, org, fSendMailToResend);
	}

	public void send(final ParameterVO parameterVO, final String to,  final String object, final String text,
			final Organism org, final Boolean fSendMailToResend) {
		send(parameterVO, to, null, object, text, org, fSendMailToResend);
	}
	
	public void send(final ParameterVO parameterVO, final String to, final List<String> tocc, final String object, final String text,
			final Organism org, final Boolean fSendMailToResend) { // CREATE CLASS MAIL UTIL FOR SEND METHODS
		String listCC = "";
		final String host = parameterVO.getEmailHost();

		final String[] emailPass = getEmailToUse(parameterVO);
		final String email = emailPass[0];
		final String password = emailPass[1];
		final String port = String.valueOf(parameterVO.getEmailPort());

		final Session session = Session.getInstance(getMailProperties(host, port), new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, password);
			}
		});

		try {
			final MimeMessage message = new MimeMessage(session);
			message.setFrom(getForm(org, email));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			if (tocc != null && !tocc.isEmpty()) {
				for (String cc : tocc) {
					message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
				}
				listCC = UtilClass.listToString(tocc);
			}

			message.setSubject(object);
			message.setSubject(object, "UTF-8");
			message.setContent(text, "text/html; Charset=UTF-8");

			Multipart multipart = new MimeMultipart("related");
			final MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(text, "text/html;Charset=UTF-8");
			multipart.addBodyPart(htmlPart);

			multipart = getMultipart(parameterVO, multipart, text, org);

			message.setContent(multipart);

			Transport.send(message);

			LOGGER.info("Sending email : {} to {}", object, to);

		} catch (final MessagingException e) {
			LOGGER.error("Problem occured while SENDING email : {} To : {}  : EXCEPTION : {}", object, to, e);
			preparedEmailToResend(org, new Date(), to, listCC, object, text, fSendMailToResend);
		} catch (final Exception e) {
			LOGGER.error("Problem occured while SENDING email : {} To : {} : EXCEPTION : {}", object, to, e);
			preparedEmailToResend(org, new Date(), to, listCC, object, text, fSendMailToResend);
		}
	}

	public void sendCCI(final String to, final List<String> tocc, final String object, final String text,
			final Organism org, final Boolean fSendMailToResend) { // CREATE CLASS MAIL UTIL FOR SEND METHODS
		String listCC = "";
		final String host = parameterVO.getEmailHost();

		final String[] emailPass = getEmailToUse(parameterVO);
		final String email = emailPass[0];
		final String password = emailPass[1];
		final String port = String.valueOf(parameterVO.getEmailPort());

		final Session session = Session.getInstance(getMailProperties(host, port), new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, password);
			}
		});

		try {
			final MimeMessage message = new MimeMessage(session);
			message.setFrom(getForm(org, email));
			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to));
			if (tocc != null && !tocc.isEmpty()) {
				for (String cc : tocc) {
					message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
				}
				listCC = UtilClass.listToString(tocc);
			}
			message.setSubject(object);
			message.setSubject(object, "UTF-8");
			message.setContent(text, "text/html; Charset=UTF-8");

			Multipart multipart = new MimeMultipart("related");
			final MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(text, "text/html;Charset=UTF-8");
			multipart.addBodyPart(htmlPart);

			multipart = getMultipart(parameterVO ,multipart, text, org);

			message.setContent(multipart);

			Transport.send(message);

			LOGGER.info("Sending email : {} to {}", object, to);

		} catch (final SendFailedException e) {
			LOGGER.error("Problem occured while SENDING email : {} To : {}  : EXCEPTION : {}", object, to, e);
			preparedEmailToResend(org, new Date(), to, listCC, object, text, fSendMailToResend);
		} catch (final MessagingException e) {
			LOGGER.error("Problem occured while SENDING email : {} To : {}  : EXCEPTION : {}", object, to, e);
			preparedEmailToResend(org, new Date(), to, listCC, object, text, fSendMailToResend);
		} catch (final Exception e) {
			LOGGER.error("Problem occured while SENDING email : {} To : {} : EXCEPTION : {}", object, to, e);
			preparedEmailToResend(org, new Date(), to, listCC, object, text, fSendMailToResend);
		}
	}

	protected Properties getMailProperties(final String host, final String port) {
		final Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.setProperty("charset", "utf-8");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.ssl.protocols", "TLSv1.3");
		return props;
	}

	protected Multipart getMultipart(ParameterVO parameterVO, Multipart multipart, String text, Organism org) throws MessagingException {
		multipart = getDefaultMultipart(multipart);
		if (text.contains("cid:logo_org")) {
			if (org.getLogoPath() != null && !org.getLogoPath().isEmpty() && !org.getLogoPath().contains("default")) {
				final int startIndex = parameterVO.getAliasPath().length();
				final int endIndex = org.getLogoPath().length();
				final String realPath = parameterVO.getImagePath() + org.getLogoPath().substring(startIndex, endIndex);

				final File orgImage = new File(realPath);
				if (orgImage.exists()) {
					multipart.addBodyPart(getImagePart(orgImage, "<logo_org>"));
				} else {
					multipart.addBodyPart(
							getImagePart("email_templates" + File.separator + "logo-sensipro.png", "<logo_org>"));
				}

			} else {
				multipart.addBodyPart(getImagePart("email_templates" + File.separator + "logo-sensipro.png", "<logo_org>"));
			}
			multipart.addBodyPart(
					getImagePart("email_templates" + File.separator + "headerImageRight.jpg", "<headerImageRight>"));
		}

		if (text.contains("cid:dataprotect_header")) {
			multipart.addBodyPart(
					getImagePart("email_templates" + File.separator + "logo-sensipro.png", "<dataprotect_header>"));
			multipart.addBodyPart(
					getImagePart("email_templates" + File.separator + "header-admin.png", "<headerImage>"));
		}

		multipart.addBodyPart(getImagePart("email_templates" + File.separator + "logo-sensipro.png", "<logo_module>"));
		return multipart;
	}

	protected Multipart getDefaultMultipart(Multipart multipart) throws MessagingException {
		multipart.addBodyPart(getImagePart("email_templates" + File.separator + "icon-twitter.png", "<twitter>"));
		multipart.addBodyPart(getImagePart("email_templates" + File.separator + "icon-face.png", "<facebook>"));
		multipart.addBodyPart(getImagePart("email_templates" + File.separator + "icon-linkedin.png", "<linkedin>"));
		multipart.addBodyPart(
				getImagePart("email_templates" + File.separator + "footerImageRight.png", "<footerImageRight>"));

		return multipart;
	}

	protected MimeBodyPart getImagePart(final String imgPath, final String headerName) {
		try {
			final MimeBodyPart imagePart = new MimeBodyPart();
			ClassPathResource resources = new ClassPathResource("images" + File.separator + imgPath);
			final DataSource ds1 = new FileDataSource(resources.getFile());
			imagePart.setDataHandler(new DataHandler(ds1));
			imagePart.setHeader("Content-ID", headerName);
			imagePart.setFileName(resources.getFilename());
			imagePart.setDisposition(MimeBodyPart.INLINE);
			return imagePart;
		} catch (final MessagingException | IOException e) {
			LOGGER.error("Error in getImagePart : {}", e);
			return null;
		}
	}

	protected MimeBodyPart getImagePart(final File logo, final String headerName) {
		try {
			final MimeBodyPart imagePart = new MimeBodyPart();
			final DataSource ds1 = new FileDataSource(logo);
			imagePart.setDataHandler(new DataHandler(ds1));
			imagePart.setHeader("Content-ID", headerName);
			imagePart.setFileName(logo.getPath());
			imagePart.setDisposition(MimeBodyPart.INLINE);
			return imagePart;
		} catch (final MessagingException e) {
			LOGGER.error("Error in getImagePart : {}", e);
			return null;
		}
	}

	protected String[] getEmailToUse(ParameterVO parameterVO) {
		String[] sendingCridential = new String[2];
		envois++;
		if (envois <= Constants.OUTLOOK_LIMIT_PER_MIN) {
			LOGGER.debug(envois + " - getEmailToUse() - " + parameterVO.getEmailAdress());
			sendingCridential[0] = parameterVO.getEmailAdress();
			sendingCridential[1] = parameterVO.getEmailPasswowrd();
		} else {
			LOGGER.debug(envois + " - getEmailToUse() - " + parameterVO.getEmailAdress1());
			sendingCridential[0] = parameterVO.getEmailAdress1();
			sendingCridential[1] = parameterVO.getEmailPasswowrd1();
			if (envois == Constants.OUTLOOK_LIMIT_PER_MIN * 2) {
				envois = 0;
			}
		}
		return sendingCridential;
	}

	InternetAddress getForm(Organism org, String email) throws AddressException, UnsupportedEncodingException {
		if (StringUtils.containsIgnoreCase(org.getName(), Constants.TANGER)
				&& StringUtils.containsIgnoreCase(org.getName(), Constants.PORT))
			return new InternetAddress(email, Constants.NOM_EXPEDITEUR, "UTF-8");
		return new InternetAddress(email);
	}

	protected void preparedEmailToResend(final Organism organisme, final Date dateEchec, final String sendTo,
			final String sendToCopy, final String object, final String text, final Boolean forceResend) {
		EmailToResend emailToResend;
		if (this instanceof MailPhishingUtil) {
			emailToResend = new EmailToResend(organisme, new Date(), sendTo, sendToCopy, object, text, forceResend,
					TypeCompte.PHISHING);
		} else if (this instanceof MailScormUtil) {
			emailToResend = new EmailToResend(organisme, new Date(), sendTo, sendToCopy, object, text, forceResend,
					TypeCompte.SCORM);
		} else {
			emailToResend = new EmailToResend(organisme, new Date(), sendTo, sendToCopy, object, text, forceResend,
					TypeCompte.SENSIPRO);
		}
		emailToResendService.createEmaiToResend(emailToResend);
	}
}
