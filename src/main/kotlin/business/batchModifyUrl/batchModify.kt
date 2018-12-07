package business.batchModifyUrl

import business.stockdata.SingletonJsoup

/**
 * Created by Chendk on 2018/9/29
 */
fun main(args: Array<String>) {
    val stockCodeHtml = SingletonJsoup.soup(
            "http://yapi.51dinghuo.cc/project/98/interface/api/cat_408",
            "yapi.51dinghuo.cc",
            mapOf(
                "Cookie" to "_yapi_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjY3LCJpYXQiOjE1MzgwMTM4NDEsImV4cCI6MTUzODYxODY0MX0.gckx2fxhmqLJyIuSn2KXO3h5yO64DTj0arMLVmlClyE; _yapi_uid=67; uid=hw8eX7Vy53UfXef05_w4CnAN3gw5x-ompFjO11Fqpl8Z4Yix6hdYhUPBXpjuHmiEfG3VyzHOT15d0C7rcJlqsXKNbnZTrLMe_lRWO1o8EdIm_vlViUg9LOOLG1f0ty39rGqCAh-gM0TUXiVA3jCEQNN7cRrxezC5Fwc3314sjTELerDBBKruJfrqYGu9Pb4Adbd7SxOj2T7mzOaaW-HYWgD6oG4fp480sH7fCkwg251t_tYTDnOADEHteMi0mzuHf7cvbaALypf5xtsJFlJzR5ZGMG33fjnu0bGoKS8FWswDw_tClXuwkVGvPp23fYVd97nuTpoAMJssaFwn2gWRIDv0IVnV4BD5FooAIPfQbWp4qQZkmzcy8nCFGEJe7bS_mWBD3mY2Ht56jJTmgpHKozpMhl1XHfNA6rk3WOLuN-GR0inRUGlL6DiC7Bkg0wG4HXgSoihzxrXpBSooIbso6I2YdjCGzd8VTVIDQYEcmUYqPRtqnKeYDrGZWUtJvNc8KRMRNz2szXiLiJ2e9Kr4lcccg1ARn61-H29uSBo9Z0ddyIPlXnLZqVcMMwThDTYs5KSeAA_ISIJTaw9nfQXLiBW7i4eLpTiun-MhFHjvgM3_dCbH7pjAcr-sO5_i39v59H5UX8Vy7t9Cx9xEytZz-_BCzAcPqJGJR28luHy_qkp03BGQpdC3B9pFmCS_jeKfamNH4C_6EiEyxGBQoFbUBAdJ-EnjdNuVVZ9_bOMEGeHWPepKhK3lUswchxPMPgT-mI4mrck8mC2Fs3ngzSHY6xST5_rnew5Emiyb9fmXpleColQQUIQcqMWBAVo-15l55rAG9kmml-4E-630ibHGc7V5wkQSTGXgFHMSd7p7-yUBKr-5_LufoYR9PFsu8koA09BYJuLN248nSQ8vy6FLR_rr3fu2fimGGQfZeRFHspCrLAPsaWtM9-kI3HRovoXamYIUEKLAGGEpt6UI72r2GyvOFO12oNhDuQLUtRXswt6IhA4IVCEuoHiQNJjcc1Rywcd2Q9g3v9Kg-VJjRzfGVMp4raCjzJXeDArM0C0PnrdVEdrUvI0RIATgEj_nsUcfD7RH5YK5mUNr2PxOh1UCHHAHrcxxt-1tahZOzDMgqt3NGXdmW1j0Ar9WwfuApI4owodUHwcurPF0kcKDK-hSIZ1dBb2RJ-WSvMXzI9u1AMGDw2RctGHMTM6kFkAJTQS67oHaSNujUuZHh1Tgddh-JAop2MhgCBjVrfFTTk2L_qjXyncpgQhmqPrCkfgDfOxTfiwlNCR9FKTrL7HXh0MfQZczqkdFM4sFMsraKU-8I-inNt-iR0tgiApYCOFoU7lwox30xaDh7QBuzJ9K24AfEF21_XRneiyXg9u9h6nIeIzGKK9BEXRdi1LpaO0nKJpUlUxjyjZTvJhD0avtS3tCmzTqkhDK6VEHI7wz90YQZKXRBgDPu3whxZxJjHIbxDuodic-YgANTyWoG7CSTI4F5o0bQazOoTrnQMhVwNgXy4CKSbn2D_TRXiGL7vK3uywt7yguuPySUMXnuKCQnP5qoVZccCRQMR_KtS3tLFvqug3eBitx1k2nujjFSYOhgYVCzmrN0yD3sP04u_cG35Tj8E6taHG0AOMRPVdZR0Pa0wuA089IXtKpNKS2Lmztn-B7ujvkFKmACYL0Z4vhEcBOZHQ_m1T_gffo17xhMbnriWyEfCBZ0LjlVLfp4LlKDHwcsTmeaLGJuej-qWmYoRGQMPmaSpMQVXbJHTE_iuQeXfQdp_yw8lIAzm12Rq3srVVT0NvFARIiDxajCgomvzlFFeP3fCmWZzEkBerwBc3JgJ-f14B6LfiHl9VddjmAtN2tod5jNlm8_3cZt7DqTACmpXeHYyMXjxHqHikH6StonprAl0X-_7834z-Z-uOM_x6SvZSlNSqgyfqVEZ0f1OtLsGOPy1aYG8pbHXLcykbx86InPwM_OQP74g6SyatjtRtSt8QHtfXlP2T2PsqFPz5bYgixljC-hZffp4oJmd0BicU."
            ))
    println(stockCodeHtml)
}