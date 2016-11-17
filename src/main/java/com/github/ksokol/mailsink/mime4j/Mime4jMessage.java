package com.github.ksokol.mailsink.mime4j;

import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.address.Address;
import org.apache.james.mime4j.dom.address.Mailbox;

import java.util.Date;
import java.util.List;

/**
 * @author Kamill Sokol
 */
public final class Mime4jMessage {

    private final Message message;
    private final Mime4jMessageBody body;

    public Mime4jMessage(Message message) {
        this.message = message;
        this.body = new Mime4jMessageBody(message);
    }

    public String getMessageId() {
        return message.getMessageId();
    }

    public String getSender() {
        Mailbox mailbox = message.getFrom().get(0);
        if(mailbox.getName() != null) {
            return String.format("%s <%s>", mailbox.getName(), mailbox.getAddress());
        }
        return mailbox.getAddress();
    }

    public String getRecipient() {
        //TODO add support for multiple recipients
        Address address = message.getTo().get(0);
        return address.toString();
    }

    public String getSubject() {
        return message.getSubject();
    }

    public Date getDate() {
        return message.getDate();
    }

    public String getPlainTextPart() {
        try {
            return body.getPlainTextPart();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getHtmlTextPart() {
        try {
            return body.getHtmlTextPart();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<Mime4jAttachment> getAttachments() {
        try {
            return body.getAttachments();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}