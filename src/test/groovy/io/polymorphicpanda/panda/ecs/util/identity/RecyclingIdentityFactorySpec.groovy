package io.polymorphicpanda.panda.ecs.util.identity

/**
 * @author Ranie Jade Ramiso
 */
class RecyclingIdentityFactorySpec extends BaseIdentityFactorySpec<RecyclingIdentityFactory> {


    def "should reuse freed up ids"(int max) {
        setup:
            def identityFactory = getIdentityFactory()

            def tracker = new HashSet<Integer>()

            // generate the ids
            (1..max).each {
                tracker << identityFactory.generate()
            }

            // free everything
            tracker.each {
                identityFactory.free(it)
            }


            // clear the tracker
            tracker.clear()


        when:
            (1..max).each {
                tracker << identityFactory.generate()
            }

        then:
            tracker.size() == max

        where:
            max     | _
            1000    | _
            10000   | _
            100000  | _
    }

    @Override
    RecyclingIdentityFactory getIdentityFactory() {
        return IdentityFactories.recycling()
    }
}
