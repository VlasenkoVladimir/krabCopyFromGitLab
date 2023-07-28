package com.krab51.webapp.domain.enums;

/**
 * Статус ловушки
 */
public enum TrapStatus {
    /** Готова для использования */
    READY,
    /** Используется */
    USED,
    /** Неисправна */
    BROKEN,
    /** Утеряна */
    LOST,
    /** Неизвестен */
    UNKNOWN
}