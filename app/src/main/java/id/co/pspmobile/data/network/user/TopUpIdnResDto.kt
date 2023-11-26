package id.co.pspmobile.data.network.user

data class TopUpIdnResDto(
	val id: String,
	val dateTimeval : String,
	val accountIdval : String,
	val companyIdval : String,
	val companyNameval : String,
	val accountNumberval : String,
	val accountNameval : String,
//	val BillReqDto billReq;
	val status: String,
	val message: String,
	val callBackDateTime: String,
//	val CallBack callBack;,
	val trxId: String,
	val repeateCount: Int
)
