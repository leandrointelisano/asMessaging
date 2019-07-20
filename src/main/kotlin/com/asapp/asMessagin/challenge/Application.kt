import com.asapp.asMessagin.challenge.controller.Routes
import com.asapp.asMessagin.challenge.injection.ControllerModule
import com.asapp.asMessagin.challenge.injection.HttpModule
import com.asapp.asMessagin.challenge.injection.ServiceModule
import com.asapp.asMessagin.challenge.persistence.MessagePersistence
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.name.Names
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.handler.gzip.GzipHandler
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import spark.servlet.SparkApplication
import spark.servlet.SparkFilter
import javax.servlet.FilterConfig


class Main {

    companion object {

        private val LOGGER = LoggerFactory.getLogger(Main::class.java)
        private val CONTEXT_PATH = "/"

        @JvmStatic
        fun main(args: Array<String>) {
            Main().run()
        }
    }

    fun run() {
        Database.connect("jdbc:sqlite:./data.db", "org.sqlite.JDBC")

        transaction { SchemaUtils.create(MessagePersistence.Users, MessagePersistence.Messages, MessagePersistence.UserLogin) }

        val injector = Guice.createInjector(
            HttpModule(),
            ControllerModule(),
            ServiceModule()
        )

        val handler = ServletContextHandler().apply {
            contextPath = CONTEXT_PATH
            gzipHandler = GzipHandler()
            listOf(
                FilterHolder(AppFilter(injector))
            ).forEach({ h -> addFilter(h, "*", null) })

        }

        try {
            val server = Server(QueuedThreadPool())
            server.addConnector(ServerConnector(server).apply { port = 9290 })
            server.handler = handler
            server.start()
            server.join()
        } catch (e: Exception) {
            LOGGER.error("Error starting the application", e)
            System.exit(1)
        }

    }

    class AppFilter(private val injector: Injector) : SparkFilter() {

        override fun getApplications(filterConfig: FilterConfig?): Array<SparkApplication> {
            return arrayOf(SparkApp(injector))
        }

    }

    class SparkApp(private val injector: Injector) : SparkApplication {

        override fun init() {
            injector.getInstance(Routes::class.java).register()
        }
    }

    private fun Injector.jettyExecutorInstance() =
        getInstance(Key.get(QueuedThreadPool::class.java, Names.named("jettyExecutor")))



}