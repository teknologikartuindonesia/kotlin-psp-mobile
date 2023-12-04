package id.co.pspmobile.data.network.user

data class TopUpIdnResDto(
	val id: String,
	val dateTimeval : String,
	val accountIdval : String,
	val companyIdval : String,
	val companyName : String,
	val accountNumber : String,
	val accountName : String,
	val billReq: BillReq?,
	val status: String,
	val message: String,
	val callBackDateTime: String,
//	val CallBack callBack;,
	val trxId: String,
	val repeateCount: Int
)

data class BillReq(
	val bill_key: String,
	val branch_code: String,
)
