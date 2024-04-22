namespace com.sap.attachments;

using {
    cuid,
    managed
} from '@sap/cds/common';

type StatusCode : String enum {
    UNSCANNED;
    INFECTED;
    NO_SCANNER;
    CLEAN;
    FAILED;
}

entity Statuses @cds.autoexpose @readonly {
    key code : StatusCode;
        text : localized String(255);
}

type Status     : Association to Statuses;

aspect MediaData @(_is_media_data) {
    content    : LargeBinary; // stored only for db-based services
    mimeType   : String;
    fileName   : String;
    documentId : String @readonly;
    status     : Status @readonly;
    scannedAt  : Timestamp @readonly;
}

aspect Attachments : cuid, managed, MediaData {
    note : String;
    url  : String;
}
