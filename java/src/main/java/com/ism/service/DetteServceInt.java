package com.ism.service;

import com.ism.core.Database.DetteRepoListInt;
import com.ism.core.Database.Service;
import com.ism.entities.Dette;

public interface DetteServceInt extends Service<Dette, DetteRepoListInt> {
  void archiverSolider();
  Dette searchDette(int id);
}
