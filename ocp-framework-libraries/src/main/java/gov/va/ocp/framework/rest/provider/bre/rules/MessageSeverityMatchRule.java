package gov.va.ocp.framework.rest.provider.bre.rules;

import gov.va.ocp.framework.messages.Message;
import gov.va.ocp.framework.messages.MessageSeverity;
import gov.va.ocp.framework.util.Defense;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.http.HttpStatus;

import java.util.Set;

/**
 * The Class MessageSeverityMatchRule is a rule used to match a Message solely based on Severity.
 *
 * @author jshrader
 */
public final class MessageSeverityMatchRule implements MessagesToHttpStatusRule {

	/** The severity to match. */
	private final MessageSeverity severityToMatch;

	/** The http status. */
	private final HttpStatus httpStatus;

	/**
	 * Instantiates a new message severity match rule.
	 *
	 * @param severityToMatch the severity to match
	 * @param httpStatus the http status
	 */
	public MessageSeverityMatchRule(final MessageSeverity severityToMatch, final HttpStatus httpStatus) {
		super();
		Defense.notNull(severityToMatch, "severityToMatch cannot be null!");
		Defense.notNull(httpStatus, "httpStatus cannot be null!");
		this.severityToMatch = severityToMatch;
		this.httpStatus = httpStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.va.ocp.framework.rest.provider.MessagesToHttpStatusRule#eval(java.util.Set)
	 */
	@Override
	public HttpStatus eval(final Set<Message> messagesToEval) {
		if (messagesToEval != null) {
			for (final Message message : messagesToEval) {
				if (severityToMatch.equals(message.getSeverity())) {
					return httpStatus;
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
