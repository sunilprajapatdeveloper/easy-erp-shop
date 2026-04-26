package nextpos.app.nextpos.model.enums;

public enum ExchangeRateSource {
    API, // External providers (Frankfurter, Fixer, etc.)
    MANUAL, // Admin/user entered
    COMPANY // Company-level override
}
