/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sam.wachingmovies.MB;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author SamFisher
 */
@Named(value = "commonManagedBean")
@SessionScoped
public class CommonManagedBean implements Serializable {

    /**
     * Creates a new instance of CommonManagedBean
     */
    public CommonManagedBean() {
    }
    
}
