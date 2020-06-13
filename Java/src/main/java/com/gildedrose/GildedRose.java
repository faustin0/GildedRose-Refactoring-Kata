package com.gildedrose;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public Optional<Item> getItemByName(String name) {
        return Stream.of(items)
                .filter(item -> item.name.equals(name))
                .findFirst();
    }

    public void updateQuality() {
        for (Item item : items) {
            if (!item.name.equals("Aged Brie") && !item.name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                if (item.quality > 0) {
                    if (!item.name.equals("Sulfuras, Hand of Ragnaros")) {
                        item.quality = item.quality - 1;
                    }
                }
            } else {
                if (item.quality < 50) {
                    item.quality = item.quality + 1;

                    if (item.name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                        if (item.sellIn < 11) {
                            if (item.quality < 50) {
                                item.quality = item.quality + 1;
                            }
                        }

                        if (item.sellIn < 6) {
                            if (item.quality < 50) {
                                item.quality = item.quality + 1;
                            }
                        }
                    }
                }
            }

            if (!item.name.equals("Sulfuras, Hand of Ragnaros")) {
                item.sellIn = item.sellIn - 1;
            }

            if (item.sellIn < 0) {
                if (!item.name.equals("Aged Brie")) {
                    if (!item.name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                        if (item.quality > 0) {
                            if (!item.name.equals("Sulfuras, Hand of Ragnaros")) {
                                item.quality = item.quality - 1;
                            }
                        }
                    } else {
                        item.quality = item.quality - item.quality;
                    }
                } else {
                    if (item.quality < 50) {
                        item.quality = item.quality + 1;
                    }
                }
            }
        }


        ItemUpdater itemUpdater = Stream.<Function<Item, Optional<ItemUpdater>>>of(
                this::maxQualityGuard,
                this::legendaryGuard,
                this::decreaser
        ).map(f -> f.apply(items[2]))
                .reduce(Optional.empty(), this::ap)
                .orElse(ItemUpdater.noOp());


        itemUpdater.run();
    }


    Optional<ItemUpdater> maxQualityGuard(Item toCheck) {
        return toCheck.quality > 50
                ? Optional.empty()
                : Optional.of(ItemUpdater.noOp());
    }


    Optional<ItemUpdater> legendaryGuard(Item toCheck) {
        if (toCheck.name.equals("Sulfuras, Hand of Ragnaros")) {
            return Optional.empty();
        }
        return Optional.of(ItemUpdater.noOp());
    }

    Optional<ItemUpdater> decreaser(Item item) {
        return Optional.of(() -> item.quality--);
    }


    public Optional<ItemUpdater> ap(Optional<ItemUpdater> o1, Optional<ItemUpdater> o2) {
        return o1.flatMap(itemUpdater1 -> o2.map(itemUpdater2 -> itemUpdater1.andThen(itemUpdater2)));
    }

    @FunctionalInterface
    public interface ItemUpdater extends Runnable {

        static ItemUpdater noOp() {
            return () -> {
            };
        }

        default ItemUpdater andThen(Runnable after) {
            Objects.requireNonNull(after);
            return () -> {
                run();
                after.run();
            };
        }

    }
}