package com.krab51.webapp.domain.enums;

/**
 * Статус путевки
 */
public enum KrabOrderStatus {
    /** Выдана и не закрыта */
    OPENED,
    /** До закрытия осталось меньше суток */
    WARNING,
    /** Закрыта нормально */
    CLOSED,
    /** Просрочена */
    EXPIRED,
    /** Закрыта просроченной */
    CLOSED_EXPIRED
}