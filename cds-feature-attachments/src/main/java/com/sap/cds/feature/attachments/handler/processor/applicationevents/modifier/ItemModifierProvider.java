package com.sap.cds.feature.attachments.handler.processor.applicationevents.modifier;

import java.util.List;

import com.sap.cds.ql.cqn.Modifier;

public interface ItemModifierProvider {

	Modifier getBeforeReadDocumentIdEnhancer(List<String> mediaAssociations);

}
