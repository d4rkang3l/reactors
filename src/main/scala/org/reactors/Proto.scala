package org.reactors



import scala.reflect.ClassTag
import org.reactors.common.Reflect



/** A prototype for instantiating a reactor that takes specific parameters.
 * 
 *  @tparam I         type of the reactor
 */
final class Proto[+I <: Reactor[_]] private[reactors] (
  val clazz: Class[_],
  val params: Seq[Any],
  val scheduler: String = null,
  val eventQueueFactory: EventQueue.Factory = null,
  val name: String = null,
  val channelName: String = "main",
  val transport: String = "reactors.udp"
) {

  /** Instantiates and returns the reactor.
   */
  def create(): I = Reflect.instantiate(clazz, params).asInstanceOf[I]

  /** Associates the specified scheduler and returns the new `Proto` object.
   *
   *  Note that the scheduler name needs to be registered with the `ReactorSystem`
   *  object.
   *
   *  @param sname               name of the scheduler
   *  @return                    a new `Proto` object
   */
  def withScheduler(sname: String): Proto[I] =
    new Proto(clazz, params, sname, eventQueueFactory, name, channelName, transport)

  /** Associates the specified event queue type and returns the new `Proto` object.
   *  
   *  @param f                   event queue factory, used to instantiate the event
   *                             queue object
   *  @return                    a new `Proto` object
   */
  def withEventQueue(f: EventQueue.Factory): Proto[I] =
    new Proto(clazz, params, scheduler, f, name, channelName, transport)

  /** Associates the name for the new reactor and returns the new `Proto` object.
   */
  def withName(nm: String): Proto[I] =
    new Proto(clazz, params, scheduler, eventQueueFactory, nm, channelName, transport)

  /** Associates the main channel name to the new reactor, and returns the `Proto`.
   */
  def withChannelName(cnm: String): Proto[I] =
    new Proto(clazz, params, scheduler, eventQueueFactory, name, cnm, transport)

  /** Associates the transport name, and returns the new `Proto`.
   */
  def withTransport(tname: String): Proto[I] =
    new Proto(clazz, params, scheduler, eventQueueFactory, name, channelName, tname)

}


object Proto {

  /** Creates prototype for instantiating an reactor that takes no parameters.
   *
   *  @tparam I         type of the reactor, must be a concrete type, or its class tag
   *                    must be in scope
   *  @return           a new prototype of an reactor of type `T`
   */
  def apply[I <: Reactor[_]: ClassTag] =
    new Proto[I](implicitly[ClassTag[I]].runtimeClass.asInstanceOf[Class[I]], Seq())

  /** Creates prototype for instantiating a reactor that takes specific parameters.
   *
   *  @tparam I         type of the reactor, must be a concrete type, or its class tag
   *                    must be in scope
   *  @param clazz      class that describes the reactor
   *  @param params     parameters for instantiating the prototype
   *  @return           a new prototype of a reactor of type `T` with the specified
   *                    parameters
   */
  def apply[I <: Reactor[_]: ClassTag](params: Any*) =
    new Proto[I](implicitly[ClassTag[I]].runtimeClass.asInstanceOf[Class[I]], params)

}