package gov.va.bip.framework.transfer.transform;

import gov.va.bip.framework.rest.provider.ProviderResponse;
import gov.va.bip.framework.service.DomainResponse;

public abstract class MessagesAwareDomainToProvider<D extends DomainResponse, P extends ProviderResponse> extends AbstractDomainToProvider<D, P> {

    @Override
    public P convert(D domainObject) {
        P providerObject = getProviderObject();
        domainObject.getMessages().forEach(
                domainMessage -> providerObject.addMessage(domainMessage.getSeverity(), domainMessage.getKey(), domainMessage.getText(), domainMessage.getHttpStatus())
        );
        return providerObject;
    }

    protected abstract P getProviderObject();
}