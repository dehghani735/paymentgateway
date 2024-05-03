CREATE TABLE client
(
    code                        VARCHAR(100) NOT NULL, -- The client identifier given by user.
    merchant_code               VARCHAR(100) NOT NULL, -- The merchant code which is the owner of this client.
    name                        VARCHAR(500) NOT NULL,
    api_key                     VARCHAR(10)  NOT NULL, -- Is Unique. Will not change ever.
    secret_key                  VARCHAR(50)  NOT NULL, -- Could be changed later by user.
    ledger_account              VARCHAR(100) NOT NULL,
    is_active                   BOOLEAN      NOT NULL DEFAULT TRUE,
    payment_facilitator_details TEXT,
    created_at                  TIMESTAMPTZ  NOT NULL,
    modified_at                 TIMESTAMPTZ  NOT NULL,
    version                     INT          NOT NULL DEFAULT 0,
    PRIMARY KEY (code)
);

CREATE TABLE client_config
(
    id                     BIGSERIAL    NOT NULL,
    code                   VARCHAR(100) NOT NULL REFERENCES client (code),
    notify_mobile_number   VARCHAR(25),
    notify_with_sms        BOOLEAN      NOT NULL DEFAULT FALSE,
    transaction_fee_config TEXT         NOT NULL,
    created_at             TIMESTAMPTZ  NOT NULL,
    PRIMARY KEY (id)
);

-- psp table definition.
CREATE TABLE psp
(
    name                           VARCHAR(20) NOT NULL,
    payment_facilitation_supported BOOLEAN     NOT NULL,
    is_active                      BOOLEAN     NOT NULL DEFAULT TRUE,
    iin                            BIGINT      NOT NULL,
    facilitator_alias              VARCHAR(50) NOT NULL,
    payment_facilitator_iban       VARCHAR(60) NOT NULL,
    facilitator_acceptor_code      VARCHAR(20) NOT NULL,
    force_card_number_in_psp       BOOLEAN     NOT NULL, -- Indicates whether PSP has support for forcing a card number for payment.
    mask_card_before_verify        BOOLEAN     NOT NULL, -- Indicates whether PSP provides the masked card number to us before verifying the payment.
    force_national_code_in_psp     BOOLEAN     NOT NULL, -- Indicates whether PSP has support to force one to pay with one of his/her own card numbers.
    cutoff                         TIME        NOT NULL,
    created_at                     TIMESTAMPTZ NOT NULL,
    modified_at                    TIMESTAMPTZ NOT NULL,
    version                        INT         NOT NULL DEFAULT 0,
    PRIMARY KEY (name)
);

CREATE TABLE terminal
(
    id                         UUID                       NOT NULL DEFAULT gen_random_uuid(),
    psp_terminal_id            VARCHAR(50)                NOT NULL,
    psp_merchant_number        VARCHAR(100), -- Is present when the client is registered for payment facilitation
    client_code                VARCHAR(100)               NOT NULL REFERENCES client (code),
    psp_name                   VARCHAR(50)                NOT NULL REFERENCES psp (name),
    type                       terminal_type              NOT NULL,
    direct_settlement          BOOLEAN                    NOT NULL,
    payment_facilitator_status payment_facilitator_status NOT NULL DEFAULT 'UNDEFINED',
    settlement_ibans           TEXT,         -- IBANs available for Shaparak settlement. This will be set only for the payment-facilitation terminals.
    active_settlement_iban     VARCHAR(60),  -- Active IBAN used for Shaparak settlement. This will be set only for the payment-facilitation terminals.
    shaparak_tracking_number   VARCHAR(25),  -- Provided by Shaparak after registering a terminal in Shaparak.
    psp_tracking_number        VARCHAR(150), -- The PSP tracking number for registering a terminal in Shaparak.
    logo                       BYTEA,
    logo_file_name             VARCHAR(100),
    logo_mime_type             VARCHAR(50),
    priority                   SMALLINT                   NOT NULL DEFAULT 0,
    is_active                  BOOLEAN                    NOT NULL DEFAULT TRUE,
    amount_variance            BIGINT                     NOT NULL DEFAULT 0,
    fee_variance               BIGINT                     NOT NULL DEFAULT 0,
    currency                   currency                   NOT NULL DEFAULT 'IRR',
    created_at                 TIMESTAMPTZ                NOT NULL,
    modified_at                TIMESTAMPTZ                NOT NULL,
    version                    INT                        NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE TABLE purchase
(
    id                       BIGSERIAL                             NOT NULL,
    client_code              VARCHAR(100) REFERENCES client (code) NOT NULL,
    client_config_id         BIGINT REFERENCES client_config (id)  NOT NULL,
    terminal_id              UUID REFERENCES terminal (id),
    amount                   BIGINT                                NOT NULL,
    wage                     BIGINT   DEFAULT 0                    NOT NULL,
    currency                 currency                              NOT NULL,
    callback_url             VARCHAR(1024)                         NOT NULL,
    state                    purchase_state                        NOT NULL,
    client_reference_number  VARCHAR(50)                           NOT NULL,
    client_trace_number      VARCHAR(50),
    expiration_date          TIMESTAMPTZ                           NOT NULL,
    user_identifier          VARCHAR(50)                           NOT NULL,
    payer_mobile_number      VARCHAR(25),
    payer_card_number        VARCHAR(25),
    payer_national_code      VARCHAR(20),
    description              VARCHAR(256),
    additional_data          TEXT,
    psp_reference_number     VARCHAR(100),
    psp_rrn                  VARCHAR(100),
    psp_trace_number         VARCHAR(100),
    psp_masked_card_number   VARCHAR(50),
    psp_card_owner           VARCHAR(200),
    psp_fail_reason          psp_error,
    psp_callback_request     TEXT,
    count                    BIGINT, -- The count of all purchases of the client from start of the jalali month.
    fee                      BIGINT,
    fee_payment_type         fee_payment_type,
    ledger_reference_number  VARCHAR(100),
    wlt_balance              BIGINT,
    fee_balance              BIGINT,
    shw_balance              BIGINT,
    verification_retry_count SMALLINT DEFAULT 0                    NOT NULL,
    init_payer_ip            VARCHAR(50),
    redirect_payer_ip        VARCHAR(50),
    psp_settled              BOOLEAN                               NOT NULL DEFAULT FALSE,
    created_at               TIMESTAMPTZ                           NOT NULL,
    modified_at              TIMESTAMPTZ                           NOT NULL,
    verified_at              TIMESTAMPTZ,
    psp_settled_at           TIMESTAMPTZ,
    shaparak_settled_at      TIMESTAMPTZ,
    version                  INTEGER  DEFAULT 0                    NOT NULL,
    PRIMARY KEY (id)
);