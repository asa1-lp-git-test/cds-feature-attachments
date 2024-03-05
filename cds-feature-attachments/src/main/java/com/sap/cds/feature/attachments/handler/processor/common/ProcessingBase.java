package com.sap.cds.feature.attachments.handler.processor.common;

import java.util.Map;
import java.util.Objects;

import com.sap.cds.feature.attachments.generation.cds4j.com.sap.attachments.Attachments;

public abstract class ProcessingBase {

	protected boolean doesDocumentIdExistsBefore(Map<?, Object> oldData) {
		return Objects.nonNull(oldData.get(Attachments.DOCUMENT_ID));
	}

}
