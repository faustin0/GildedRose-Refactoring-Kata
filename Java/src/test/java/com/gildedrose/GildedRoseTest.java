package com.gildedrose;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    @Test
    void foo() {
        Item[] items = new Item[]{new Item("foo", 0, 0)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.getItemByName("foo")).isPresent();
    }

    @Test
    void AgedBrie_shouldIncreaseQuality() {
        Item[] items = new Item[]{new Item("Aged Brie", 0, 10)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(12, app.items[0].quality);
        assertEquals("Aged Brie", app.items[0].name);
    }

    @Test
    void qualityShouldNeverExceed50() {
        Item[] items = new Item[]{new Item("expensive", 0, 50)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        app.updateQuality();
        app.updateQuality();

        assertThat(app.getItemByName("expensive").get().quality)
                .isLessThan(50);
    }

    @Test
    void guardCheck() {
        Item[] items = new Item[]{
                new Item("Sulfuras, Hand of Ragnaros", 0, 40),
                new Item("expensive", 0, 50),
                new Item("normal", 0, 5),
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertThat(app.getItemByName("expensive").get().quality)
                .isLessThan(50);
    }

    @Test
    void applicativeTest() {
        Optional<GildedRose.ItemUpdater> itemUpdater1 = Optional.of(GildedRose.ItemUpdater.noOp());
        Optional<GildedRose.ItemUpdater> itemUpdater2 = Optional.empty();

        Optional<GildedRose.ItemUpdater> ap = new GildedRose(new Item[]{}).ap(itemUpdater1, itemUpdater2);
        assertThat(ap).isEmpty();
    }
}
