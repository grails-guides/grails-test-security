package example.grails

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class BootStrap {

    def init = { servletContext ->

        def authorities = ['ROLE_BOSS', 'ROLE_EMPLOYEE']
        authorities.each { String authority ->
            if ( !SecurityRole.findByAuthority(authority) ) {
                new SecurityRole(authority: authority).save()
            }
        }

        if ( !User.findByUsername('sherlock') ) {
            def u = new User(username: 'sherlock', password: 'elementary')
            u.save()
            new UserSecurityRole(user: u, securityRole: SecurityRole.findByAuthority('ROLE_BOSS')).save()
        }

        if ( !User.findByUsername('watson') ) {
            def u = new User(username: 'watson', password: '221Bbakerstreet')
            u.save()
            new UserSecurityRole(user: u, securityRole: SecurityRole.findByAuthority('ROLE_EMPLOYEE')).save()
        }
    }
    def destroy = {
    }
}
