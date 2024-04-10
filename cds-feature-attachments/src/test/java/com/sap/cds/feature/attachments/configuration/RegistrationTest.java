package com.sap.cds.feature.attachments.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.sap.cds.feature.attachments.handler.applicationservice.CreateAttachmentsHandler;
import com.sap.cds.feature.attachments.handler.applicationservice.DeleteAttachmentsHandler;
import com.sap.cds.feature.attachments.handler.applicationservice.ReadAttachmentsHandler;
import com.sap.cds.feature.attachments.handler.applicationservice.UpdateAttachmentsHandler;
import com.sap.cds.feature.attachments.handler.draftservice.DraftCancelAttachmentsHandler;
import com.sap.cds.feature.attachments.handler.draftservice.DraftPatchAttachmentsHandler;
import com.sap.cds.feature.attachments.service.AttachmentMalwareScanService;
import com.sap.cds.feature.attachments.service.AttachmentService;
import com.sap.cds.feature.attachments.service.handler.DefaultAttachmentsServiceHandler;
import com.sap.cds.services.Service;
import com.sap.cds.services.ServiceCatalog;
import com.sap.cds.services.environment.CdsEnvironment;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.outbox.OutboxService;
import com.sap.cds.services.persistence.PersistenceService;
import com.sap.cds.services.runtime.CdsRuntime;
import com.sap.cds.services.runtime.CdsRuntimeConfigurer;

class RegistrationTest {

	private Registration cut;
	private CdsRuntimeConfigurer configurer;
	private ServiceCatalog serviceCatalog;
	private CdsEnvironment environment;
	private PersistenceService persistenceService;
	private AttachmentService attachmentService;
	private OutboxService outboxService;
	private ArgumentCaptor<Service> serviceArgumentCaptor;
	private ArgumentCaptor<EventHandler> handlerArgumentCaptor;

	@BeforeEach
	void setup() {
		cut = new Registration();

		configurer = mock(CdsRuntimeConfigurer.class);
		CdsRuntime cdsRuntime = mock(CdsRuntime.class);
		when(configurer.getCdsRuntime()).thenReturn(cdsRuntime);
		serviceCatalog = mock(ServiceCatalog.class);
		when(cdsRuntime.getServiceCatalog()).thenReturn(serviceCatalog);
		environment = mock(CdsEnvironment.class);
		when(cdsRuntime.getEnvironment()).thenReturn(environment);
		persistenceService = mock(PersistenceService.class);
		attachmentService = mock(AttachmentService.class);
		outboxService = mock(OutboxService.class);
		serviceArgumentCaptor = ArgumentCaptor.forClass(Service.class);
		handlerArgumentCaptor = ArgumentCaptor.forClass(EventHandler.class);
	}

	@Test
	void serviceIsRegistered() {
		cut.services(configurer);

		verify(configurer, times(2)).service(serviceArgumentCaptor.capture());
		var services = serviceArgumentCaptor.getAllValues();
		assertThat(services).hasSize(2);

		var attachmentServiceFound = services.stream().anyMatch(service -> service instanceof AttachmentService);
		var malwareScanServiceFound = services.stream().anyMatch(service -> service instanceof AttachmentMalwareScanService);

		assertThat(attachmentServiceFound).isTrue();
		assertThat(malwareScanServiceFound).isTrue();
	}

	@Test
	void handlersAreRegistered() {
		when(serviceCatalog.getService(PersistenceService.class, PersistenceService.DEFAULT_NAME)).thenReturn(persistenceService);
		when(serviceCatalog.getService(AttachmentService.class, AttachmentService.DEFAULT_NAME)).thenReturn(attachmentService);
		when(serviceCatalog.getService(OutboxService.class, OutboxService.PERSISTENT_UNORDERED_NAME)).thenReturn(outboxService);

		cut.eventHandlers(configurer);

		var handlerSize = 7;
		verify(configurer, times(handlerSize)).eventHandler(handlerArgumentCaptor.capture());
		var handlers = handlerArgumentCaptor.getAllValues();
		assertThat(handlers).hasSize(handlerSize);
		isHandlerForClassIncluded(handlers, DefaultAttachmentsServiceHandler.class);
		isHandlerForClassIncluded(handlers, CreateAttachmentsHandler.class);
		isHandlerForClassIncluded(handlers, UpdateAttachmentsHandler.class);
		isHandlerForClassIncluded(handlers, DeleteAttachmentsHandler.class);
		isHandlerForClassIncluded(handlers, ReadAttachmentsHandler.class);
		isHandlerForClassIncluded(handlers, DraftPatchAttachmentsHandler.class);
		isHandlerForClassIncluded(handlers, DraftCancelAttachmentsHandler.class);
	}

	private void isHandlerForClassIncluded(List<EventHandler> handlers, Class<? extends EventHandler> includedClass) {
		var isHandlerIncluded = handlers.stream().anyMatch(handler -> handler.getClass() == includedClass);
		assertThat(isHandlerIncluded).isTrue();
	}

}
