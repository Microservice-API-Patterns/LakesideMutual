// @flow

import React from "react"
import { Divider } from "semantic-ui-react"
import moment from "moment"

export type Props = {
  date: Date,
}

const GroupSeparator = (props: Props) => (
  <li className="groupSeparator">
    <Divider clearing section horizontal>
      {moment(props.date).fromNow()}
    </Divider>
  </li>
)

export default GroupSeparator
