package gov.va.bip.framework.audit;

import java.util.ArrayList;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.google.auto.service.AutoService;

import gov.va.bip.framework.audit.annotation.Auditable;

/**
 * The Class AuditableProcessor that extends abstract annotation processor 
 * for @Auditable annotation processors.
 */
@SupportedAnnotationTypes(AuditableProcessor.AUDITABLE_ANNOTATION_PKG)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class AuditableProcessor extends AbstractProcessor {

	public static final String AUDITABLE_ANNOTATION_PKG = "gov.va.bip.framework.audit.annotation.Auditable";

	/**
	 * Process.
	 *
	 * @param annotations the annotations
	 * @param roundEnv    the round environment
	 * @return true, if successful
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (TypeElement annotation : annotations) {
			for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
				processingEnv.getMessager().printMessage(Kind.NOTE, "found @Auditable on " + element);
				Auditable auditable = element.getAnnotation(Auditable.class);
				processingEnv.getMessager().printMessage(Kind.NOTE, "Annotation " + auditable);
				if (auditable != null) {
					if (StringUtils.isNotBlank(auditable.auditDate())) {
						parseAuditableDateExpression(element, auditable);
					}
					processAuditableModifiersCheck(element);
				}
			}
		}
		return true;
	}

	/**
	 * Process audit date expression parsing for @Auditable .
	 *
	 * @param element the element
	 * @param auditable the auditable
	 */
	private void parseAuditableDateExpression(final Element element, final Auditable auditable) {
		try {
			String auditDate = auditable.auditDate();
			ExpressionParser expressionParser = new SpelExpressionParser();
			Expression expression = expressionParser.parseExpression(auditDate);
			Object parsedSpel = expression.getValue();
			if (parsedSpel instanceof String) {
				auditDate = (String) parsedSpel;
			} else if (parsedSpel instanceof ArrayList) {
				auditDate = (String) ((ArrayList<?>) parsedSpel).get(0);
			}
			processingEnv.getMessager().printMessage(Kind.NOTE, "Parsed expression string value for auditDate " + auditDate);
		} catch (final Exception e) {
			processingEnv.getMessager().printMessage(Kind.ERROR,
					"Invalid auditDate Expression String for @Auditable on " + element + "\nException " + e, element);
		}
	}

	/**
	 * Process auditable modifiers check.
	 *
	 * @param element the element
	 */
	private void processAuditableModifiersCheck(final Element element) {
		Set<Modifier> modifiers = element.getModifiers();
		// methods with @Auditable annotations MUST be public and NOT final
		if (!modifiers.contains(Modifier.PUBLIC)) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "method wasn't public for @Auditable on " + element,
					element);
		}
		if (modifiers.contains(Modifier.FINAL)) {
			processingEnv.getMessager().printMessage(Kind.ERROR,
					"method shouldn't be final for @Auditable on " + element, element);
		}
	}
}
