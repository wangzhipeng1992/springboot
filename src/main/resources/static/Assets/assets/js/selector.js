function selectUser(type, nameCtl, idCtl, CompanyCode) {
    var val;
    //0:选择任意
    //1:单选人
    //2:多选人
    //3:单选部门
    //4:多选部门
    //5:单选岗位
    //6:多选岗位
    //7:单选组
    //8:多选组
    //val = window.showModalDialog(path + "/Modules/Ultimus.UWF.OrgChart/SelectAllUser.aspx?Type=" + type, null, "dialogWidth=800px;dialogHeight=500px");
    if (type == "1"||type=="Contract") {
        CompanyCode =ChangeComanyCode($("#UserInfo1_fld_COMPANYCODE").val());
    }
    window.open(path + "/Modules/Ultimus.UWF.OrgChart/SelectAllUser.aspx?Type=" + type + "&nameCtl=" + nameCtl + "&idCtl=" + idCtl + "&Companycode=" + CompanyCode, 'newwindow', 'height=500,width=800,top=100,left=200,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
    //    if (!val) {
    //        val = window.returnValue;
    //    }
    //    if (val) {
    //        var obj = eval(val);
    //        var names = "";
    //        var ids = "";
    //        if (obj) {
    //            for (i = 0; i < obj.length; i++) {
    //                if (i == 0) {
    //                    names += obj[i].Name;
    //                    ids += obj[i].LoginName + "|" + obj[i].Type;
    //                }
    //                else {
    //                    names += "," + obj[i].Name;
    //                    ids += "," + obj[i].LoginName + "|" + obj[i].Type;
    //                }
    //            }
    //        }

    //        $("#" + nameCtl).val(names);
    //        if (idCtl) {
    //            $("#" + idCtl).val(ids);
    //        }
    //    }
}

function selectPage(sql, order, displayField, displayFieldCaption, displayFieldWidth, title) {
    str = path + "/Modules/Ultimus.UWF.Common/SelectPage.aspx?sql=" + sql + "&order=" + order + "&query=" + displayField + "&caption=" + displayFieldCaption + "&width=" + displayFieldWidth + "&title=" + title;
    str = encodeURI(str);
    val = window.showModalDialog(str, null, "scroll:1;status:0;help:0;dialogWidth=800px;dialogHeight=480px");
    if (!val) {
        val = window.returnValue;
    }
    return val;
}


function selectPage(sql, order, displayField, displayFieldCaption, displayFieldWidth, title, dbName) {
    str = path + "/Modules/Ultimus.UWF.Common/SelectPage.aspx?dbName=" + dbName + "&sql=" + sql + "&order=" + order + "&query=" + displayField + "&caption=" + displayFieldCaption + "&width=" + displayFieldWidth + "&title=" + title;
    str = encodeURI(str);
    val = window.showModalDialog(str, null, "scroll:1;status:0;help:0;dialogWidth=800px;dialogHeight=480px");
    if (!val) {
        val = window.returnValue;
    }
    return val;
}


//转大写
function setMoneyCAP(num) {
    //num = num.replace(",", "").replace(",", "").replace(",", "").replace(",", "").replace(",", "").replace(",", "");
    //num = num.replace("￥", "").replace("￥", "").replace("￥", "").replace("￥", "").replace("￥", "").replace("￥", "");

    if (num == "" || num == null) {
        return;
    }

    if (isNaN(num)) {
        alert("只能输入数字和小数点！");
        return;
    }

    currencyDigits = num;
    //最大值
    var MAXIMUM_NUMBER = 99999999999.99;
    //定义数字大写汉字符号
    var CN_ZERO = "零";
    var CN_ONE = "壹";
    var CN_TWO = "贰";
    var CN_THREE = "叁";
    var CN_FOUR = "肆";
    var CN_FIVE = "伍";
    var CN_SIX = "陆";
    var CN_SEVEN = "柒";
    var CN_EIGHT = "捌";
    var CN_NINE = "玖";
    var CN_TEN = "拾";
    var CN_HUNDRED = "佰";
    var CN_THOUSAND = "仟";
    var CN_TEN_THOUSAND = "万";
    var CN_HUNDRED_MILLION = "亿";
    var CN_SYMBOL = "";
    var CN_DOLLAR = "圆";
    var CN_TEN_CENT = "角";
    var CN_CENT = "分";
    var CN_INTEGER = "整";

    //临时变量
    var integral;           // Represent integral part of digit number.
    var decimal;            // Represent decimal part of digit number.
    var outputCharacters;   // The output result.
    var parts;
    var digits, radices, bigRadices, decimals;
    var zeroCount;
    var i, p, d;
    var quotient, modulus;

    // Validate input string:
    currencyDigits = currencyDigits.toString();
    if (currencyDigits == "") {
        alert("输入为空，不能进行转换！");
        return "";
    }
    if (currencyDigits.match(/[^,.\d]/) != null) {
        alert("数值中存在非法字符！");
        return "";
    }
    if ((currencyDigits).match(/^((\d{1,3}(,\d{3})*(.((\d{3},)*\d{1,3}))?)|(\d+(.\d+)?))$/) == null) {
        alert("非法的数值格式！");
        return "";
    }

    // Normalize the format of input digits:
    currencyDigits = currencyDigits.replace(/,/g, "");      // Remove comma delimiters.
    currencyDigits = currencyDigits.replace(/^0+/, "");     // Trim zeros at the beginning.

    //如果数值超过最大值的范围
    if (Number(currencyDigits) > MAXIMUM_NUMBER) {
        alert("数值过大，无法完成转换！");
        return "";
    }

    // Process the coversion from currency digits to characters:
    // Separate integral and decimal parts before processing coversion:
    parts = currencyDigits.split(".");
    if (parts.length > 1) {
        integral = parts[0];
        decimal = parts[1];
        decimal = decimal.substr(0, 2);     // Cut down redundant decimal digits that are after the second.
    }
    else {
        integral = parts[0];
        decimal = "";
    }

    // Prepare the characters corresponding to the digits:
    digits = new Array(CN_ZERO, CN_ONE, CN_TWO, CN_THREE, CN_FOUR, CN_FIVE, CN_SIX, CN_SEVEN, CN_EIGHT, CN_NINE);
    radices = new Array("", CN_TEN, CN_HUNDRED, CN_THOUSAND);
    bigRadices = new Array("", CN_TEN_THOUSAND, CN_HUNDRED_MILLION);
    decimals = new Array(CN_TEN_CENT, CN_CENT);

    // Start processing:
    outputCharacters = "";

    // Process integral part if it is larger than 0:
    if (Number(integral) > 0) {
        zeroCount = 0;
        for (i = 0; i < integral.length; i++) {
            p = integral.length - i - 1;
            d = integral.substr(i, 1);
            quotient = p / 4;
            modulus = p % 4;
            if (d == "0") {
                zeroCount++;
            }
            else {
                if (zeroCount > 0) {
                    outputCharacters += digits[0];
                }
                zeroCount = 0;
                outputCharacters += digits[Number(d)] + radices[modulus];
            }

            if (modulus == 0 && zeroCount < 4) {
                outputCharacters += bigRadices[quotient];
            }
        }

        outputCharacters += CN_DOLLAR;
    }

    // Process decimal part if there is:
    if (decimal != "") {
        for (i = 0; i < decimal.length; i++) {
            d = decimal.substr(i, 1);
            if (d != "0") {
                outputCharacters += digits[Number(d)] + decimals[i];
            }
        }
    }

    // Confirm and return the final output string:
    if (outputCharacters == "") {
        outputCharacters = CN_ZERO + CN_DOLLAR;
    }

    if (decimal == "" || decimal == "00" || decimal == "0") {
        outputCharacters += CN_INTEGER;
    }

    outputCharacters = CN_SYMBOL + outputCharacters;
    return outputCharacters;
}
