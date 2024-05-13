package de.patri.ck.clipperdbx.common.model;

import java.io.Serializable;

public class Clipper implements Serializable {

  public int      _id;
  public int   set_id;

  public String name;
  public String image;
  public String own;

  public String tags;
  public String cap;
  public String pin;

  public String datum;
  public String lastedit;

  public Clipper(int _id, int set_id, String name, String image, String own, String datum, String lastedit) {
    this._id      = _id;
    this.set_id   = set_id;
    this.name     = name;
    this.image    = image;
    this.own      = own;
    this.datum    = datum;
    this.lastedit = lastedit;
  }

  public Clipper(int set_id, String name, String image, String own, String tags, String cap, String pin, String datum, String lastedit) {
    this.set_id   = set_id;
    this.name     = name;
    this.image    = image;
    this.own      = own;
    this.tags     = tags;
	this.cap      = cap;
	this.pin      = pin;
    this.datum    = datum;
    this.lastedit = lastedit;
  }

  public int get_Id() {
        return _id;
    }
  public void set_Id(int _id) {
        this._id = _id;
    }

  public int getSetId() {
        return set_id;
    }
  public void setSetId(int set_id) {
        this.set_id = set_id;
    }

  public String getName() {
        return name;
    }
  public void setName(String name) {
        this.name = name;
    }

  public String getImage() {
        return image;
    }
  public void setImage(String image) {
        this.image = image;
    }

  public String getOwn() {
        return own;
    }
  public void setOwn(String own) {
        this.own = own;
    }

  public String getCap() {
        return cap;
    }
  public void setCap(String cap) {
        this.cap = cap;
    }

  public String getPin() {
        return pin;
    }
  public void setPin(String pin) {
        this.pin = pin;
    }

  public String getDatum() {
        return datum;
    }
  public void setDatum(String datum) {
        this.datum = datum;
    }

  public String getLastEdit() {
        return lastedit;
    }
  public void setLastEdit(String lastedit) {
        this.lastedit = lastedit;
    }

}
