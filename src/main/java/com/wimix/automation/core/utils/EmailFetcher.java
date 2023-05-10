package com.wimix.automation.core.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import com.wimix.automation.core.configuration.SentryConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.awaitility.Awaitility;

import javax.mail.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class EmailFetcher {

    @SneakyThrows
    public static String waitGetUntilRegistrationId() {
        AtomicReference<String> id = new AtomicReference<>();
        Awaitility
                .given()
                .pollInterval(5, TimeUnit.SECONDS)
                .atMost(120, TimeUnit.SECONDS)
                .pollInSameThread()
                .ignoreExceptions()
                .until(() -> {
                    id.set(extractRegistrationId());
                    return true;
                });
        return id.get();
    }

    @SneakyThrows
    public static String waitGetUntilRegistrationId2() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        AtomicReference<String> id = new AtomicReference<>();
        Retry.whileTrue(5000, 120000, () -> {
            try {
                id.set(extractRegistrationId());
                return false;
            } catch (EmailNotReceivedException | MessagingException | GeneralSecurityException | IOException e) {
                return true;
            }
        }, "Unexpectedly, email was not received");
        stopWatch.stop();
        log.debug("Time spent: " + stopWatch.getTime(TimeUnit.SECONDS) + "s");
        return id.get();
    }

    private static String extractRegistrationId() throws MessagingException, EmailNotReceivedException, GeneralSecurityException, IOException {
        String host = "pop.gmail.com";
        String mailStoreType = "pop3s";
        String username = SentryConfig.getRealEmail();
        String password = SentryConfig.getRealPassword();
        String messageBody = fetch(host, mailStoreType, username, password);
        return messageBody
                .replaceAll("\n", "")
                //.split(SentryConfig.getBaseUrl() + "signup/")[1]
                .split("Ваш логин:")[0].trim();
    }

    private static String fetch(String pop3Host, String storeType, String user, String password) throws GeneralSecurityException, MessagingException, IOException, EmailNotReceivedException {
        MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
        socketFactory.setTrustAllHosts(true);
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "pop3");
        properties.put("mail.pop3.host", pop3Host);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3s.ssl.socketFactory", socketFactory);

        Session emailSession = Session.getDefaultInstance(properties);

        Store store = emailSession.getStore(storeType);
        store.connect(pop3Host, user, password);

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        Message[] messages = emailFolder.getMessages();

        log.debug("Trying to get registration email...");

        List<Message> filteredMessages = new ArrayList<>();
        for (Message message : messages) {
            if (message.getSubject().contains("Your Sentry Derivatives Password")) {
                filteredMessages.add(message);
            }
        }
        //get the newest message from inbox
        String messageBody;
        try {
            messageBody = getMessageBodyOrContentType(filteredMessages.get(filteredMessages.size() - 1), false);
        } catch (IndexOutOfBoundsException e) {
            throw new EmailNotReceivedException(null);
        }

        emailFolder.close(false);
        store.close();
        return messageBody;
    }

    private static String getMessageBodyOrContentType(Part p, final boolean returnContentType) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            return returnContentType ? p.getContentType() : s;
        }
        if (p.isMimeType("multipart/alternative")) {
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) {
                        text = getMessageBodyOrContentType(bp, returnContentType);
                    }
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getMessageBodyOrContentType(mp.getBodyPart(i), returnContentType);
                if (s != null) {
                    return s;
                }
            }
        }
        return null;
    }
}