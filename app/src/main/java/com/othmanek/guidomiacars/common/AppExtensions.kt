package com.othmanek.guidomiacars.common

import java.text.DecimalFormat


fun Double.getPriceToDisplay(): String = DecimalFormat("#,###").format(this).toString()
