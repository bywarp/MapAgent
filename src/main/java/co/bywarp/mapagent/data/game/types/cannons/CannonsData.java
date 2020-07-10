/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.game.types.cannons;

import co.bywarp.mapagent.data.game.GameData;
import co.bywarp.mapagent.data.game.GameDataType;
import co.bywarp.mapagent.data.game.team.presets.PlayersTeam;
import co.bywarp.mapagent.data.game.types.cannons.blocks.CannonDataBlock;
import co.bywarp.mapagent.utils.DataUtils;

public class CannonsData extends GameData {

    public CannonsData() {
        super("Cannons",
                GameDataType.CANNONS,
                DataUtils.inlineList(
                        new PlayersTeam()
                ),
                new CannonDataBlock()
        );
    }

}
