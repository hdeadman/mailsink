package com.github.ksokol.mailsink.repository;

import com.github.ksokol.mailsink.entity.Mail;
import com.github.ksokol.mailsink.entity.MailAttachment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Kamill Sokol
 */
@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class MailRepositoryTest {

    private static final LocalDateTime EPOCH_UTC = LocalDateTime.now(Clock.fixed(Instant.EPOCH, ZoneId.of("UTC")));

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private TestEntityManager em;

    @Before
    @After
    public void cleanUp() throws Exception {
        mailRepository.deleteAll();
    }

    @Test
    public void shouldPersistMail() throws Exception {
        Date cratedAt = toDate(EPOCH_UTC);

        Mail mail = new Mail();
        mail.setMessageId("messageId");
        mail.setSubject("subject");
        mail.setSender("sender");
        mail.setRecipient("recipient");
        mail.setText("plain");
        mail.setHtml("html");
        mail.setSource("source");
        mail.setCreatedAt(cratedAt);

        mail = em.persist(mail);
        Optional<Mail> expected = mailRepository.findById(mail.getId());
        Mail expectedMail = expected.get();
        assertThat(expectedMail.getId(), is(mail.getId()));
        assertThat(expectedMail.getMessageId(), is("messageId"));
        assertThat(expectedMail.getSender(), is("sender"));
        assertThat(expectedMail.getRecipient(), is("recipient"));
        assertThat(expectedMail.getSubject(), is("subject"));
        assertThat(expectedMail.getText(), is("plain"));
        assertThat(expectedMail.getHtml(), is("html"));
        assertThat(expectedMail.getSource(), is("source"));
        assertThat(expectedMail.getCreatedAt(), is(cratedAt));
    }

    @Test
    public void shouldOrderMailsByCreationDateDescending() throws Exception {
        Mail mail1 = new Mail();
        mail1.setCreatedAt(toDate(EPOCH_UTC));
        em.persist(mail1);

        Mail mail2 = new Mail();
        mail2.setCreatedAt(toDate(EPOCH_UTC.plusMinutes(1)));
        em.persist(mail2);

        assertThat(
                "should order mails by creation date descending",
                mailRepository.findAllOrderByCreatedAtDesc(),
                contains(hasProperty("id", is(1L)), hasProperty("id", is(0L)))
        );
    }

    @Test
    public void shouldSaveMailWithAttachment() throws Exception {
        Mail mail = new Mail();
        MailAttachment mailAttachment = new MailAttachment();
        mailAttachment.setMail(mail);
        mail.setAttachments(Collections.singletonList(mailAttachment));

        em.persist(mail);

        assertThat(
                "should save mail with attachment",
                mailRepository.findAllOrderByCreatedAtDesc(),
                contains(hasProperty("attachments", hasSize(1)))
        );
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }
}
