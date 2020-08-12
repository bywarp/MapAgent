/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.game.types.tdm;

import co.bywarp.mapagent.data.game.GameData;
import co.bywarp.mapagent.data.game.GameDataType;
import co.bywarp.mapagent.data.game.types.tdm.teams.BlueTeam;
import co.bywarp.mapagent.data.game.types.tdm.teams.GreenTeam;
import co.bywarp.mapagent.data.game.types.tdm.teams.RedTeam;
import co.bywarp.mapagent.data.game.types.tdm.teams.YellowTeam;
import co.bywarp.mapagent.utils.DataUtils;

public class TeamDeathmatchData extends GameData {

    public TeamDeathmatchData() {
        super("Team Deathmatch",
                GameDataType.TEAM_DEATHMATCH,
                DataUtils.inlineList(
                        new RedTeam(),
                        new YellowTeam(),
                        new GreenTeam(),
                        new BlueTeam()
                )
        );
    }

}
