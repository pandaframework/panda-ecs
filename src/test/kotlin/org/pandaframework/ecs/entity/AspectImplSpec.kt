package org.pandaframework.ecs.entity

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.DefaultComponentIdentityManager

/**
 * @author Ranie Jade Ramiso
 */
class AspectImplSpec: SubjectSpek<AspectImpl>({
    val componentIdentityManager = DefaultComponentIdentityManager()

    class Component1: Component
    class Component2: Component
    class Component3: Component
    class Component4: Component

    val identities = arrayOf(
        componentIdentityManager.getIdentity(Component1::class),
        componentIdentityManager.getIdentity(Component2::class),
        componentIdentityManager.getIdentity(Component3::class),
        componentIdentityManager.getIdentity(Component4::class)
    )


    subject { AspectImpl(componentIdentityManager) }

    given("all[Component1] any[Component2, Component4] exclude[Component4]") {
        beforeEach {
            subject.all(Component1::class)
                .any(Component2::class, Component4::class)
                .exclude(Component3::class)
        }

        on("match") {
            it("should return false if Component1 is not present") {
                assertThat(
                    subject.match(identities[1]),
                    equalTo(false)
                )
            }

            it("should return false if neither Component2 and Component4 is present") {
                assertThat(
                    subject.match(
                        identities[0]
                    ),
                    equalTo(false)
                )
            }

            it("should return false if Component3 is present") {
                assertThat(
                    subject.match(
                        identities[0]
                            .or(identities[1])
                            .or(identities[2])
                    ),
                    equalTo(false)
                )
            }

            it("should return true if Component1 and either Component2 or Component4 is present") {
                arrayOf(1, 3).forEach {
                    assertThat(
                        subject.match(
                            identities[0]
                                .or(identities[it])
                        ),
                        equalTo(true)
                    )
                }

            }
        }
    }
})
