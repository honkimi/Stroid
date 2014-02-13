package com.honkimi.stroid;

import android.content.ContentValues;

/**
 * The interface which can reutrn ContentValues
 * @author kiminari.homma
 */
public interface Contentable {
    /**
     * Implement this with your DTO
     * @return ContentValues
     */
    ContentValues toContentValues();
}
