/**
call_durationB = phoneLogs.callAt(phoneLogs.numberOfCalls(PhoneLogs.FOLDER_NORMAL_CALLS)-1 
											, PhoneLogs.FOLDER_NORMAL_CALLS).getDuration() ;
			call_numberB = ((PhoneCallLog)phoneLogs.callAt(
								phoneLogs.numberOfCalls(PhoneLogs.FOLDER_NORMAL_CALLS)-1,
								PhoneLogs.FOLDER_NORMAL_CALLS)).getParticipant().getNumber();
			call_participantB = ((PhoneCallLog)phoneLogs.callAt(
					phoneLogs.numberOfCalls(PhoneLogs.FOLDER_NORMAL_CALLS)-1,
					PhoneLogs.FOLDER_NORMAL_CALLS)).getParticipant();
			Object[] lastValues = { String.valueOf(call_durationB) , call_numberB , call_participantB };
			
			synchronized(persistentObj)
			{
				persistentObj.setContents(lastValues);
				persistentObj.commit();
			}
	
			Object[] newValues = (Object[])persistentObj.getContents();
			if(newValues==null)
			{
				call_sum = 0;
				call_number = "";
			}
			else
			{
				callLog = phoneLogs.callAt(phoneLogs.numberOfCalls(PhoneLogs.FOLDER_NORMAL_CALLS)-1, 
						PhoneLogs.FOLDER_NORMAL_CALLS);
				call_number = ((PhoneCallLog)callLog).getParticipant().getNumber();
				call_duration = callLog.getDuration();
				
				call_participant = ((PhoneCallLog)callLog).getParticipant();
				
				call_sum += call_duration;
				if(call_number != call_numberB && call_duration == call_durationB && 
							call_participant != call_participantB)
				{
					callLog = phoneLogs.callAt(phoneLogs.numberOfCalls(PhoneLogs.FOLDER_NORMAL_CALLS)-1, 
							PhoneLogs.FOLDER_NORMAL_CALLS);
					call_number = ((PhoneCallLog)callLog).getParticipant().getNumber();
					call_duration = callLog.getDuration();
					call_sum += call_duration;
				}
			}
		}		

		else
		{

		}
*/