/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data;

import java.util.ArrayList;
import java.util.stream.Stream;

import com.google.common.base.Joiner;

public class MapAuthor {

    private ArrayList<String> authors;

    public MapAuthor(String... author) {
        this.authors = new ArrayList<>();

        Stream<String> stream = Stream.of(author);
        stream.forEach(authors::add);
    }

    public String getAuthorString() {
        if (authors == null || authors.isEmpty()) {
            return "None";
        }

        if (authors.size() == 1) {
            return authors.get(0);
        }

        if (authors.size() == 2) {
            return authors.get(0) + " & " + authors.get(1);
        }

        ArrayList<String> withoutLast = new ArrayList<>();
        authors
                .stream()
                .limit(authors.size() - 1)
                .forEach(withoutLast::add);

        return Joiner.on(", ").join(withoutLast) + ", and " + authors.get(authors.size() - 1);
    }

}
