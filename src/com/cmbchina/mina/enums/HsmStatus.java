package com.cmbchina.mina.enums;

public enum HsmStatus {
	IDLE {
		public String toString() {
			return "IDLE";
		}
	},
	FREE {
		public String toString() {
			return "FREE";
		}
	},
	BUSY {
		public String toString() {
			return "BUSY";
		}
	}, 
	ERROR {
		public String toString() {
			return "ERROR";
		}
	}
}
