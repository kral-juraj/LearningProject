package com.beekeeper.desktop.dialog;

import com.beekeeper.shared.entity.Taxation;
import com.beekeeper.shared.entity.TaxationFrame;

import java.util.List;

/**
 * Result class to return both Taxation and its frames from TaxationDialog.
 */
public class TaxationWithFrames {
    public final Taxation taxation;
    public final List<TaxationFrame> frames;

    public TaxationWithFrames(Taxation taxation, List<TaxationFrame> frames) {
        this.taxation = taxation;
        this.frames = frames;
    }
}
