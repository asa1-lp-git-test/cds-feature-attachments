package com.sap.cds.feature.attachments.service;

import java.io.InputStream;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import com.sap.cds.feature.attachments.generated.cds4j.com.sap.attachments.MediaData;
import com.sap.cds.feature.attachments.service.model.service.AttachmentModificationResult;
import com.sap.cds.feature.attachments.service.model.service.CreateAttachmentInput;
import com.sap.cds.feature.attachments.service.model.servicehandler.AttachmentCreateEventContext;
import com.sap.cds.feature.attachments.service.model.servicehandler.AttachmentMarkAsDeletedEventContext;
import com.sap.cds.feature.attachments.service.model.servicehandler.AttachmentReadEventContext;
import com.sap.cds.feature.attachments.service.model.servicehandler.AttachmentRestoreEventContext;
import com.sap.cds.feature.attachments.utilities.LoggingMarker;
import com.sap.cds.services.ServiceDelegator;

/**
	* Default implementation of the {@link AttachmentService} interface.
	* The main	purpose of this class is to set data in the corresponding context and
	* to call the emit method for the attachment service.
	*/
public class DefaultAttachmentsService extends ServiceDelegator implements AttachmentService {

	private static final Logger logger = LoggerFactory.getLogger(DefaultAttachmentsService.class);
	private static final Marker create_marker = LoggingMarker.ATTACHMENT_SERVICE_CREATE_METHOD.getMarker();
	private static final Marker delete_marker = LoggingMarker.ATTACHMENT_SERVICE_DELETE_METHOD.getMarker();
	private static final Marker restore_marker = LoggingMarker.ATTACHMENT_SERVICE_RESTORE_METHOD.getMarker();
	private static final Marker read_marker = LoggingMarker.ATTACHMENT_SERVICE_READ_METHOD.getMarker();

	public DefaultAttachmentsService() {
		super(AttachmentService.DEFAULT_NAME);
	}

	@Override
	public InputStream readAttachment(String documentId) {
		logger.info(read_marker, "Reading attachment with document id: {}", documentId);

		var readContext = AttachmentReadEventContext.create();
		readContext.setDocumentId(documentId);
		readContext.setData(MediaData.create());

		emit(readContext);

		return readContext.getData().getContent();
	}

	@Override
	public AttachmentModificationResult createAttachment(CreateAttachmentInput input) {
		logger.info(create_marker, "Creating attachment for entity name: {}", input.attachmentEntity().getQualifiedName());

		var createContext = AttachmentCreateEventContext.create();
		createContext.setAttachmentIds(input.attachmentIds());
		createContext.setAttachmentEntity(input.attachmentEntity());
		var mediaData = MediaData.create();
		mediaData.setFileName(input.fileName());
		mediaData.setMimeType(input.mimeType());
		mediaData.setContent(input.content());
		createContext.setData(mediaData);

		emit(createContext);

		return new AttachmentModificationResult(Boolean.TRUE.equals(createContext.getIsInternalStored()),
																																										createContext.getDocumentId(), createContext.getData().getStatusCode());
	}

	@Override
	public void markAsDeleted(String documentId) {
		logger.info(delete_marker, "Marking attachment as deleted for document id: {}", documentId);

		var deleteContext = AttachmentMarkAsDeletedEventContext.create();
		deleteContext.setDocumentId(documentId);

		emit(deleteContext);
	}

	@Override
	public void restore(Instant restoreTimestamp) {
		logger.info(restore_marker, "Restoring deleted attachment for timestamp: {}", restoreTimestamp);
		var restoreContext = AttachmentRestoreEventContext.create();
		restoreContext.setRestoreTimestamp(restoreTimestamp);

		emit(restoreContext);
	}

}
