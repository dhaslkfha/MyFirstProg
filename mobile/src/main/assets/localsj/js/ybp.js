
function ybp(str,ybpDate,lev1Kwh,lev2Kwh){
			
		var oBox=document.getElementById('main');
    	var timeTicket=null;
    	var aa=lev1Kwh;
    	var bb=lev2Kwh;
		console.log(aa+bb);
    	//var str="12340";
    	var timer=null;
    	var oBox=document.getElementById('box');
        var myChart = echarts.init(document.getElementById('main')); 
        
		var option = {
		    tooltip : {
		        show: false,
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		    	show:false,
		        data:[]
		    },
		   grid:{
		    x:0,
		    y:0,
		    x2:0,
		    y2:0,
			borderWidth:0
		  },
		  animation:false,
		    series : [
		      {
			            name:'个性化仪表盘',
			            type:'gauge',
			            center : ['50%', '50%'],    // 默认全局居中
			            radius : ['93%', '100%'],
			            startAngle: 225,
			            endAngle : -45,
			            min: 0,                     // 最小值
			            max: 100,                   // 最大值
			            precision: 0,               // 小数精度，默认为0，无小数点
			            splitNumber: 12,             // 分割段数，默认为5
			            axisLine: {            // 坐标轴线
			                show: true,        // 默认显示，属性show控制显示与否
			                lineStyle: {       // 属性lineStyle控制线条样式
			                    color: [[1/3, '#d9dfdd'],[2/3, '#d9dfdd'],[1, '#d9dfdd']],  
			                    width: 8
			                }
			            },
			            axisTick: {            // 坐标轴小标记
			                show: true,        // 属性show控制显示与否，默认不显示
			                splitNumber: 5,    // 每份split细分多少段
			                length :8,         // 属性length控制线长
			                lineStyle: {       // 属性lineStyle控制线条样式
			                    //color: '#eee',
			                    width:0,
			                   // type: 'solid'
			                }
			            },
			            axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
			                show: false,
			                
			            },
			            splitLine: {           // 分隔线
			                show: true,        // 默认显示，属性show控制显示与否
			                length :17,         // 属性length控制线长
			                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
			                    color: '#d9dfdd',
			                    width: 3,
			                    type: 'solid'
			                }
			            },
			            pointer : {
			            	show:false,
			                
			            },
			            title : {
			                show : false,
			            },
			            
			           
			            data:[{value: '', name: 'KWH'}]
			        },
		        {
			            name:'个性化仪表盘',
			            type:'gauge',
			            center : ['50%', '50%'],    // 默认全局居中
			            radius : [0, '93%'],
			            startAngle: 225,
			            endAngle : -45,
			            min: 0,                     // 最小值
			            max: 100,                   // 最大值
			            precision: 0,               // 小数精度，默认为0，无小数点
			            splitNumber: 12,             // 分割段数，默认为5
			            axisLine: {            // 坐标轴线
			                show: true,        // 默认显示，属性show控制显示与否
			                lineStyle: {       // 属性lineStyle控制线条样式
			                     //color: [[0.2, 'lightgreen'],[0.4, '#f2c52e'],[0.8, 'skyblue'],[1, '#ff4500']],
			                    color: [[1/3, 'lightgreen'],[2/3, '#f2c52e'],[1, '#ff4500']],  
			                    //color: [[0.2, 'lightgreen'],[0.4, '#f2c52e'],[1, '#ff4500']],
			                    width: 5
			                }
			            },
			            axisTick: {            // 坐标轴小标记
			                show: true,        // 属性show控制显示与否，默认不显示
			                splitNumber: 5,    // 每份split细分多少段
			                length :8,         // 属性length控制线长
			                lineStyle: {       // 属性lineStyle控制线条样式
			                    //color: '#eee',
			                    width:0,
			                   // type: 'solid'
			                }
			            },
			            axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
			                show: true,
			                formatter: function(v){
			                	//console.log(parseInt(v));
								
			                	 v=parseInt(v);
			 					switch (v+''){
			 						case '0': return '0';
			                        case '16': return (0+aa)/2;
			                        case '33': return aa;
			                        case '50': return (aa+bb)/2;
			                        case '66': return bb;
			                        case '83': return (bb+4*bb)/2;
			                        case '100': return 4*bb;
			                        default: return '';
			                    }
			                },
			                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
			                    color: '#333'
			                }
			            },
			            splitLine: {           // 分隔线
			                show: true,        // 默认显示，属性show控制显示与否
			                length :7,         // 属性length控制线长
			                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
			                    //color: '#ccc',
			                    shadowColor:'rgba(217,223,221,0)',
			                    width: 2,
			                    type: 'solid'
			                }
			            },
			            pointer : {
			                length : '80%',
			                width : 8,      //指针
			                color : '#fc5658'
			            },
			            title : {
			                show : true,
			                offsetCenter: ['0%', -50],       // x, y，单位px
			                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
			                    color: '#333',
			                    fontSize : 15
			                }
			            },
			            
			            detail : {
			                show : true,
			                backgroundColor: 'rgba(0,0,0,0)',
			                borderWidth: 0,
			                borderColor: '#ccc',
			                width: 100,
			                height: 40,
			                offsetCenter: ['0', 85],       // x, y，单位px
			                //formatter:'{value}%\n累计用电量',
			                formatter:function(v){
			                	//cc='123'
			                	//str="123";
			                	//var num=parseInt(str);
			                	//console.log(str.charAt(0));
			                	
								//oBox.innerHTML=num;
			                	/*timer=setInterval(function(){
			                		oBox.innerHTML='';
			                			n+=10;
			                			
			                			if(n>=parseInt(num/10)*10+num%10){
			                				n=parseInt(num/10)*10+num%10;
			                				clearInterval(timer);
			                			};
			                			str=n.toString();
			                			//console.log(n);
			                			for(var i=0; i<str.length; i++){

					                		var oImg=document.createElement('img');
					                		oImg.src="img/0.png";
					                		oBox.appendChild(oImg);
					                		
					                		oImg.src="img/"+str.charAt(i)+".png";
					                	}
			                			//oImg.src="img/"+str.charAt(i)+".png";
			                		},1);*/
			                	for(var i=0; i<str.length; i++){

									var oImg=document.createElement('img');
									oImg.src="img/0.png";
									oBox.appendChild(oImg);
									
									oImg.src="img/"+str.charAt(i)+".png";
								}
			                	return '\n年累计用电量';
			                	
			                	
			                },
			                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
			                    color: '#373737',
			                    fontSize : 14
			                }
			            },
			            data:[{value: ybpDate, name: 'kWh'}]
			        }
		    ]
		};
                    
                    
        myChart.setOption(option); 
};