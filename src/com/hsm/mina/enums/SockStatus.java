package com.hsm.mina.enums;

public enum SockStatus {
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
	DOWN {
		public String toString() {
			return "DOWN";
		}
	}
}
