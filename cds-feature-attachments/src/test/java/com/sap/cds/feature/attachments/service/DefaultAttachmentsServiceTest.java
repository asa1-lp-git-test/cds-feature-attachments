package com.sap.cds.feature.attachments.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultAttachmentsServiceTest {

	private DefaultAttachmentsService cut;

	@BeforeEach
	void setup() {
		cut = new DefaultAttachmentsService();
	}

	@Test
	void dummy() {
		assertThat(cut).isNotNull();
	}

//	@Test
//	void readAttachmentDoesNotThrow() {
//		var stream = cut.readAttachment("");
//		assertThat(stream).isNull();
//	}
//
//	@Test
//	void createAttachmentReturnsDocumentId() {
//		var input = new CreateAttachmentInput(Map.of("ID", "attachmentId"), "", "", "", null);
//		var result = cut.createAttachment(input);
//		assertThat(result.documentId()).isNotEmpty();
//	}
//
//	@Test
//	void createAttachmentReturnsNotExternalStored() {
//		var input = new CreateAttachmentInput(Collections.emptyMap(), "", "", "", null);
//		var result = cut.createAttachment(input);
//		assertThat(result.isExternalStored()).isFalse();
//	}
//
//	@Test
//	void updateAttachmentReturnsDocumentId() {
//		var input = new UpdateAttachmentInput("documentId", Map.of("ID", "attachmentId"), "", "", "", null);
//		var result = cut.updateAttachment(input);
//		assertThat(result.documentId()).isNotEmpty();
//	}
//
//	@Test
//	void updateAttachmentReturnsNotExternalStored() {
//		var input = new UpdateAttachmentInput("", Collections.emptyMap(), "", "", "", null);
//		var result = cut.updateAttachment(input);
//		assertThat(result.isExternalStored()).isFalse();
//	}
//
//	@Test
//	void deleteDoesNotThrow() {
//		assertDoesNotThrow(() -> cut.deleteAttachment(""));
//	}

}
