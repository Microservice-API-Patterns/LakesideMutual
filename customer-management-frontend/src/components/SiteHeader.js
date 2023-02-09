// @flow

import React from "react"
import { Image } from "semantic-ui-react"
import { Link } from "react-router-dom"
import logo from "../images/logo.png"

const styles = {
  siteheader: {
    display: "inline-flex",
    marginTop: 20,
    alignItems: "center",
  },
  logo: { height: 65, marginRight: 10 },
  header: {
    fontSize: 28,
    lineHeight: "28px",
    fontWeight: "bold",
    color: "#192a56",
    marginBottom: 5,
  },
  subheader: {
    fontSize: 22,
    lineHeight: "22px",
    fontWeight: "lighter",
    color: "#273c75",
    margin: 0,
    padding: 0,
  },
}

export type Props = {}
const SiteHeader = (props: Props) => {
  return (
    <Link to="/">
      <div style={styles.siteheader}>
        <Image src={logo} style={styles.logo} />
        <div>
          <div style={styles.header}>Customer Management</div>
          <div style={styles.subheader}>Lakeside Mutual</div>
        </div>
      </div>
    </Link>
  )
}

export default SiteHeader
