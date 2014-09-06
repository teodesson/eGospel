/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.dao;

import com.dariawan.gospel.domain.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface MenuDao extends PagingAndSortingRepository<Menu, String> {

    @Query("select m from Menu m " +
			"where m.parent is null " +
			"order by m.level, m.order")
    public List<Menu> findTopLevelMenu();

    @Query("select m from Menu m " +
			"where m.parent.id = :id " +
			"order by m.level, m.order")
    public List<Menu> findMenuByParent(@Param("id") String id);

    public List<Menu> findByIdNotIn(List<String> ids);

}
