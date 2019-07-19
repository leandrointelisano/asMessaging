package com.asapp.asMessagin.challenge.controller

import com.asapp.asMessagin.challenge.service.LogInService
import spark.Request
import spark.Response

class LogInController ( private val logInService: LogInService){
    fun userLogIn(req: Request, res: Response)
}