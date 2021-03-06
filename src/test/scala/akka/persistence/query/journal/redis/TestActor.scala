/** Copyright (C) 2015-2017 Lightbend Inc. <http://www.lightbend.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package akka.persistence.query.journal.redis

import akka.persistence.PersistentActor
import akka.actor.Props

object TestActor {
  def props(persistenceId: String): Props =
    Props(new TestActor(persistenceId))

  case class DeleteCmd(toSeqNr: Long = Long.MaxValue)
}

class TestActor(override val persistenceId: String) extends PersistentActor {

  import TestActor.DeleteCmd

  val receiveRecover: Receive = {
    case evt: String =>
  }

  val receiveCommand: Receive = {
    case DeleteCmd(toSeqNr) =>

      deleteMessages(toSeqNr)
      sender() ! s"$toSeqNr-deleted"

    case cmd: String =>
      persist(cmd) { evt =>
        sender() ! evt + "-done"
      }
  }

}
