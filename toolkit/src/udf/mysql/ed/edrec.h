/*
  $Id: edrec.h 4069 2021-05-18 11:40:25Z yicong $

  Copyright (C) 2008 by The Regents of the University of California

  Redistribution of this file is permitted under the terms of the
  BSD license

  Date: 05/15/2008
  Author: Rares Vernica <rares (at) ics.uci.edu>
  Maintainer: Yicong Huang <yicongh1 (at) uci.edu>
*/

#include <mysql.h>

#if MYSQL_VERSION_ID >= 80000 && MYSQL_VERSION_ID < 100000 // support MySQL >= 8.0
typedef _Bool my_bool;
typedef my_ulonglong longlong;
#else // support MySQL < 8.0
#include <my_global.h>
#include <my_sys.h>
#endif

void strtolower(char *s);

const char *strpbrknot(const char *s, const char *set);

longlong edrec(UDF_INIT *initid __attribute__((unused)),
               UDF_ARGS *args,
               char *is_null,
               char *error __attribute__((unused)));


