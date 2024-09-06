package uk.org.fca.demoproject

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.google.gson.Gson

class SyncHandler : RequestHandler<MutableMap<String, Any>, String> {

    override fun handleRequest(input: MutableMap<String, Any>?, context: Context): String {
        context.getLogger().log("In handler")
        return "oh hi mark"
    }
}