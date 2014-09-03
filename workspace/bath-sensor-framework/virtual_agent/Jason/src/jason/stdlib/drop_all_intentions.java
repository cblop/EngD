//----------------------------------------------------------------------------
// Copyright (C) 2003  Rafael H. Bordini, Jomi F. Hubner, et al.
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// 
// To contact the authors:
// http://www.inf.ufrgs.br/~bordini
// http://www.das.ufsc.br/~jomi
//
//----------------------------------------------------------------------------


package jason.stdlib;

import jason.asSemantics.Circumstance;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.Event;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

/**

  <p>Internal action: <b><code>.drop_all_intentions</code></b>.
  
  <p>Description: removes all intentions from the agent's set of
  intentions (even suspended intentions are removed). 
  No event is produced.

  <p>This action changes the agent's circumstance structure by simply
  emptying the whole set of intentions (I), pending actions (PA),
  pending intentions (PI), and events in E that are not external
  events (thus generated by intentions).

  <p>Example:<ul> 

  <li> <code>.drop_all_intentions</code>.

  </ul>

  @see jason.stdlib.intend
  @see jason.stdlib.desire
  @see jason.stdlib.drop_all_desires
  @see jason.stdlib.drop_all_events
  @see jason.stdlib.drop_intention
  @see jason.stdlib.drop_desire
  @see jason.stdlib.succeed_goal
  @see jason.stdlib.fail_goal
  @see jason.stdlib.current_intention
  @see jason.stdlib.suspend
  @see jason.stdlib.suspended
  @see jason.stdlib.resume

 */
public class drop_all_intentions extends DefaultInternalAction {

    @Override public int getMinArgs() { return 0; }
    @Override public int getMaxArgs() { return 0; }

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        checkArguments(args);
        
        Circumstance C = ts.getC();
        C.clearIntentions();
        C.clearPendingIntentions();
        C.clearPendingActions();

        // drop intentions in E
        for (Event e: C.getEvents()) {
            if (e.isInternal()) {
                C.removeEvent(e);
            }
        }

        // drop intentions in PE
        for (String ek: C.getPendingEvents().keySet()) {
            Event e = C.getPendingEvents().get(ek);
            if (e.isInternal()) {
                C.removePendingEvent(ek);
            }
        }

        // cancel future events generated by .at
        at atia = (at)ts.getAg().getIA(at.atAtom);
        atia.cancelAts();
        
        return true;
    }
}