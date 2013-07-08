function CheckCtrl($scope) {
	$scope.questions = [
		{
			text:'你的孩子喜欢你摇他或者把他放在你的膝盖上等等之类的事吗？',
			critical:false,
			pass:true},
		{
			text:'你的孩子对其他孩子有兴趣吗？',
			critical:true,
			pass:true},
		{
			text:'你的孩子喜欢爬东西，像上楼梯吗？',
			critical:false,
			pass:true},
		{
			text:'你的孩子喜欢玩捉迷藏吗？',
			critical:false,
			pass:true},
		{
			text:'你的孩子会假装，例如，讲电话或照顾洋娃娃，或假装其他事情吗？',
			critical:false,
			pass:true},
		{
			text:'你的孩子曾用食指指着东西，要求某样东西吗？',
			critical:false,
			pass:true},
		{
			text:'你的孩子曾用食指指着东西，表示对某样东西有兴趣吗？',
			critical:true,
			pass:true},
		{
			text:'你的孩子会正确玩小玩具（例如车子或积木），而不是只把它们放在嘴里、随便乱动或者把它们丢掉？',
			critical:false,
			pass:true},
		{
			text:'你的孩子曾经拿东西给你（家长）看吗？',
			critical:true,
			pass:true},
		{
			text:'你的孩子会注意看着你的眼睛超过一、两秒钟吗？',
			critical:false,
			pass:true},
		{
			text:'你的孩子曾对声音过分敏感吗？（例如捂住耳朵）',
			critical:false,
			pass:false},
		{
			text:'你的孩子看着你的脸或是你的微笑时会以微笑回应吗？',
			critical:false,
			pass:true},
		{
			text:'你的孩子会模仿你吗？（例如：你扮个鬼脸，你的孩子会模仿吗？）',
			critical:true,
			pass:true},
		{
			text:'你的孩子听到别人叫他/她的名字时，他/她会回应吗？',
			critical:true,
			pass:true},
		{
			text:'如果你指着房间另一头的玩具，你的孩子会看那个玩具吗？',
			critical:true,
			pass:true},
		{
			text:'你的孩子走路吗？',
			critical:false,
			pass:true},
		{
			text:'你的孩子会看你正在看的东西吗？',
			critical:false,
			pass:true},
		{
			text:'你的孩子会在他/她的脸附近做出一些不寻常的手指头动作吗？',
			critical:false,
			pass:false},
		{
			text:'你的孩子会设法吸引你看他/她自己的活动吗？',
			critical:false,
			pass:true},
		{
			text:'你是否曾经怀疑你的孩子听力有问题？',
			critical:false,
			pass:false},
		{
			text:'你的孩子能理解别人说的话吗？',
			critical:false,
			pass:true},
		{
			text:'你的孩子有时候会两眼失焦或者没有目的地逛来逛去吗？',
			critical:false,
			pass:false},
		{
			text:'你的孩子碰到不熟悉的事物时会看着你的脸，看看你的反应吗？',
			critical:false,
			pass:true}
	];

	$scope.checkNull=function(){
		var nullNum =new Array();
		var no=0;
		angular.forEach($scope.questions, function(q) {
			no++;
			if (q.answer==null)
				nullNum.push(no);
		});
		if (nullNum.length==0)
			return "";

		var content=nullNum[0].toString();
		for (no=1;no<nullNum.length;no++){
			content+=","+nullNum[no].toString();
		}
		return content;
	};

	$scope.count={
		count1:0,
		count2:0
	};

	$scope.check = function() {
		//critical
		var count1 = 0;
		//altogether
		var count2 = 0;
		/*//null answer
		var count3=0;
		var nullQuestions={};*/
		angular.forEach($scope.questions, function(q) {
			if (q.critical)
				count1+= q.answer== q.pass.toString() ? 0 : 1;
			count2 += q.answer== q.pass.toString() ? 0 : 1;
		});

		var result=count1>=2||count2>=3 ? true:false;
		$scope.count.count1=count1;
		$scope.count.count2=count2;
		return {
			score1:count1,
			score2:count2,
			label:result? "important":"success",
			text:result? "阳性":"阴性"
		};
	};



}