grails {
    plugin {
        springsecurity {
            securityConfigType = "InterceptUrlMap"  // <1>
            filterChain {
                chainMap = [
                [pattern: '/api/**',filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'],// <2>
                [pattern: '/**', filters: 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter'] // <3>
                ]
            }
            userLookup {
                userDomainClassName = 'grails.test.security.User' // <4>
                authorityJoinClassName = 'grails.test.security.UserSecurityRole' // <4>
            }
            authority {
                className = 'grails.test.security.SecurityRole' // <4>
            }
            interceptUrlMap = [
                    [pattern: '/', access: ['permitAll']],
                    [pattern: '/error', access: ['permitAll']],
                    [pattern: '/index', access: ['permitAll']],
                    [pattern: '/index.gsp', access: ['permitAll']],
                    [pattern: '/shutdown', access: ['permitAll']],
                    [pattern: '/assets/**', access: ['permitAll']],
                    [pattern: '/**/js/**', access: ['permitAll']],
                    [pattern: '/**/css/**', access: ['permitAll']],
                    [pattern: '/**/images/**', access: ['permitAll']],
                    [pattern: '/**/favicon.ico', access: ['permitAll']],
                    [pattern: '/login/auth', access: ['ROLE_ANONYMOUS']], // <5>
                    [pattern: '/announcement/index',  access: ['ROLE_BOSS', 'ROLE_EMPLOYEE']], // <6>
                    [pattern: '/api/login',  access: ['ROLE_ANONYMOUS']], // <7>
                    [pattern: '/oauth/access_token',  access: ['ROLE_ANONYMOUS']], // <8>
                    [pattern: '/api/announcements',  access: ['ROLE_BOSS']] // <9>
            ]
        }
    }
}

