package example.grails

import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification

@SuppressWarnings(['MethodName', 'DuplicateNumberLiteral'])
@Integration
@Rollback
class ApiAnnouncementControllerSpec extends Specification {

    def 'test /api/announcements url is secured'() {
        given:
        RestBuilder rest = new RestBuilder()

        when:
        def resp = rest.get("http://localhost:${serverPort}/api/announcements") { // <1>
            accept('application/json') // <2>
            contentType('application/json') // <3>
        }

        then:
        resp.status == 401 // <4>
        resp.json.status == 401
        resp.json.error == 'Unauthorized'
        resp.json.message == 'No message available'
        resp.json.path == '/api/announcements'
    }

    def "test a user with the role ROLE_BOSS is able to access /api/announcements url"() {
        when: 'login with the sherlock'
        RestBuilder rest = new RestBuilder()
        def resp = rest.post("http://localhost:${serverPort}/api/login") { // <5>
            accept('application/json')
            contentType('application/json')
            json {
                username = 'sherlock'
                password = 'elementary'
            }
        }

        then:
        resp.status == 200
        resp.json.roles.find { it == 'ROLE_BOSS' }

        when:
        def accessToken = resp.json.access_token

        then:
        accessToken

        when:
        resp = rest.get("http://localhost:${serverPort}/api/announcements") {
            accept('application/json')
            header('Authorization', "Bearer ${accessToken}") // <6>
        }

        then:
        resp.status == 200 // <7>
    }

    def "test a user with the role ROLE_EMPLOYEE is NOT able to access /api/announcements url"() {
        when: 'login with the watson'
        RestBuilder rest = new RestBuilder()

        def resp = rest.post("http://localhost:${serverPort}/api/login") {
            accept('application/json')
            contentType('application/json')
            json {
                username = 'watson'
                password = '221Bbakerstreet'
            }
        }

        then:
        resp.status == 200
        !resp.json.roles.find { it == 'ROLE_BOSS' }
        resp.json.roles.find { it == 'ROLE_EMPLOYEE' }

        when:
        def accessToken = resp.json.access_token

        then:
        accessToken

        when:
        resp = rest.get("http://localhost:${serverPort}/api/announcements") {
            accept('application/json')
            header('Authorization', "Bearer ${accessToken}")
        }

        then:
        resp.status == 403 // <8>

    }
}
