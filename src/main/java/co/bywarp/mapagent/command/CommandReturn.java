/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.command;

/**
 * @author Struck713
 * @date Jun 11, 2017
 */
public enum CommandReturn {

	HELP_MENU(true), UNKNOWN_COMMAND(false), EXIT(true);
	
	private boolean cancel;
	
	private CommandReturn(boolean cancel) {
		this.cancel = cancel;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
	
}
